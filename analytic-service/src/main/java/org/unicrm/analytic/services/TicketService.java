package org.unicrm.analytic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.analytic.api.Status;
import org.unicrm.analytic.api.TimeInterval;
import org.unicrm.analytic.converter.TicketMapper;
import org.unicrm.analytic.dto.CurrentPage;
import org.unicrm.analytic.dto.GlobalInfo;
import org.unicrm.analytic.dto.TicketFrontDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;
import org.unicrm.analytic.exceptions.ResourceNotFoundException;
import org.unicrm.analytic.repositorys.TicketRepository;
import org.unicrm.lib.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketMapper ticketMapper;
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final DepartmentService departmentService;


    @KafkaListener(topics = "ticketTopic", containerFactory = "ticketKafkaListenerContainerFactory")
    @Transactional
    public void createOrUpdateTicket(TicketDto ticketDto) {
        Ticket ticket;
        if (ticketRepository.existsById(ticketDto.getId())) {
            if (ticketDto.getStatus().equals(Status.DELETED.getValue())) {
                ticketRepository.deleteById(ticketDto.getId());
            }
            ticket = updateTicket(ticketDto);
        } else {
            if (ticketDto.getAssigneeId() != null && ticketDto.getAssigneeDepartmentId() != null) {
                ticket = createTicket(ticketDto);
            } else return;
        }
        ticketRepository.save(ticket);
    }

    public Page<TicketFrontDto> getTicketByAssignee(UUID userId, Status status, CurrentPage currentPage) {
        Pageable pageable = PageRequest
                .of(currentPage.getPage() - 1, currentPage.getCountElements(), Sort.by("createdAt"));
        LocalDateTime between = getTimeForInterval(currentPage.getTimeInterval());
        return ticketRepository
                .findAllByAssigneeIdWithStatus(pageable, userId, status, between, LocalDateTime.now())
                .map(ticketMapper::fromEntityToFrontDto);
    }

    public Page<TicketFrontDto> getTicketByAssigneeDepartment(Long departmentId, Status status, CurrentPage currentPage) {
        Pageable pageable = PageRequest.of(currentPage.getPage() - 1, currentPage.getCountElements(), Sort.by("createdAt"));
        LocalDateTime between = getTimeForInterval(currentPage.getTimeInterval());
        return ticketRepository
                .countByAssigneeDepartmentWithStatus(pageable, departmentId, status, between, LocalDateTime.now())
                .map(ticketMapper::fromEntityToFrontDto);
    }

    private Ticket updateTicket(TicketDto ticketDto) {
        Ticket ticket = ticketRepository.findById(ticketDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + ticketDto.getId() + " не найдена"));
        ticket.setUpdatedAt(ticketDto.getUpdatedAt());
        ticket.setStatus(Status.valueOf(ticketDto.getStatus()));
        if (ticketDto.getAssigneeId() != null) {
            ticket.setAssignee(userService.findById(ticketDto.getAssigneeId()));
        }
        return ticket;
    }

    private Ticket createTicket(TicketDto ticketDto) {
        Department department = departmentService.findById(ticketDto.getAssigneeDepartmentId());
        User reporter = userService.findById(ticketDto.getReporterId());
        User assignee;
        if (ticketDto.getAssigneeId() != null) {
            assignee = userService.findById(ticketDto.getAssigneeId());
        } else {
            assignee = null;
        }
        return ticketMapper.fromTicketDto(ticketDto, reporter, assignee, department);
    }

    public GlobalInfo getInfoByAssigneeDepartment(Long departmentId, LocalDateTime beginTime, LocalDateTime endTime) {
        return GlobalInfo.builder()
                .ticketCountDone(ticketRepository
                        .countByAssigneeDepartmentWithStatus(departmentId, Status.DONE, beginTime, endTime))
                .ticketCountInProgress(ticketRepository
                        .countByAssigneeDepartmentWithStatus(departmentId, Status.IN_PROGRESS, beginTime, endTime))
                .ticketCountAccepted(ticketRepository
                        .countByAssigneeDepartmentWithStatus(departmentId, Status.ACCEPTED, beginTime, endTime))
                .ticketCountOverdue(ticketRepository
                        .countByAssigneeDepartmentWithStatus(departmentId, Status.OVERDUE, beginTime, endTime))
                .build();
    }

    public GlobalInfo getUserInfo(UUID userId, LocalDateTime beginTime, LocalDateTime endTime) {
        return GlobalInfo.builder()
                .ticketCountDone(ticketRepository
                        .countByAssigneeIdWithStatus(userId, Status.DONE, beginTime, endTime))
                .ticketCountInProgress(ticketRepository
                        .countByAssigneeIdWithStatus(userId, Status.IN_PROGRESS, beginTime, endTime))
                .ticketCountAccepted(ticketRepository
                        .countByAssigneeIdWithStatus(userId, Status.ACCEPTED, beginTime, endTime))
                .ticketCountOverdue(ticketRepository
                        .countByAssigneeIdWithStatus(userId, Status.OVERDUE, beginTime, endTime))
                .build();
    }

    public LocalDateTime getTimeForInterval(TimeInterval interval) {
        switch (interval) {
            case WEEK:
                return LocalDateTime.now().minusWeeks(1);
            case MONTH:
                return LocalDateTime.now().minusMonths(1);
            case THREE_MONTH:
                return LocalDateTime.now().minusMonths(3);
            case HALF_YEAR:
                return LocalDateTime.now().minusMonths(6);
            case YEAR:
                return LocalDateTime.now().minusYears(1);
            case DAY:
            default:
                return LocalDateTime.now().minusDays(1);
        }
    }
}
