package org.unicrm.analytic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.analytic.api.OverdueStatus;
import org.unicrm.analytic.api.Status;
import org.unicrm.analytic.api.TimeInterval;
import org.unicrm.analytic.converter.TicketMapper;
import org.unicrm.analytic.dto.TicketResponseDto;
import org.unicrm.analytic.dto.kafka.KafkaTicketDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;
import org.unicrm.analytic.exceptions.ResourceNotFoundException;
import org.unicrm.analytic.exceptions.validators.TicketValidator;
import org.unicrm.analytic.repositorys.TicketRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketMapper ticketMapper;
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final TicketValidator ticketValidator;
    private final DepartmentService departmentService;

    @KafkaListener(topics = "ticketTopic", containerFactory = "ticketKafkaListenerContainerFactory")
    @Transactional
    public void createOrUpdateTicket(KafkaTicketDto ticketDto) {
        ticketValidator.validate(ticketDto);
        Ticket ticket;
        if (ticketRepository.existsById(ticketDto.getId())) {
            if (ticketDto.getStatus().equals(Status.DELETED.getValue())) {
                ticketRepository.deleteById(ticketDto.getId());
            } else {
                updateTicket(ticketDto);
            }
        } else {
            ticket = createTicket(ticketDto);
            ticketRepository.save(ticket);

        }
    }

    private Ticket updateTicket(KafkaTicketDto ticketDto) {
        Ticket ticket = ticketRepository.findById(ticketDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + ticketDto.getId() + " не найдена"));
        ticket.setUpdatedAt(ticketDto.getUpdatedAt());
        if (ticketDto.getOverdue() != null) {
            ticket.setOverdue(OverdueStatus.valueOf(ticketDto.getOverdue()));
        }
        ticket.setStatus(Status.valueOf(ticketDto.getStatus()));
        if (ticketDto.getAssigneeId() != null) {
            ticket.setAssignee(userService.findById(ticketDto.getAssigneeId()));
        }
        return ticket;
    }

    private Ticket createTicket(KafkaTicketDto ticketDto) {
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

    public List<TicketResponseDto> getTicketByAssignee(UUID userId, Status status, TimeInterval timeInterval) {
        LocalDateTime between = getTimeForInterval(timeInterval);
        return ticketRepository
                .findAllByAssigneeIdWithStatus(userId, status, between, LocalDateTime.now()).stream()
                .map(ticketMapper::fromEntityToFrontDto).collect(Collectors.toList());
    }

    public List<TicketResponseDto> getTicketByAssigneeDepartment(Long departmentId, Status status, TimeInterval timeInterval) {
        LocalDateTime between = getTimeForInterval(timeInterval);
        return ticketRepository
                .findAllByAssigneeDepartmentWithStatus(departmentId, status, between, LocalDateTime.now()).stream()
                .map(ticketMapper::fromEntityToFrontDto).collect(Collectors.toList());
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

    public Map<String, Long> getAssigneeTicketsByStatus(UUID userId, TimeInterval time) {
        Map<String, Long> map = new HashMap<>();
        Status[] enums = Status.values();
        for (Status s : enums) {
            map.put(s.name(), ticketRepository.countByStatusAndAssigneeGroupByStatus(userId, s, getTimeForInterval(time), LocalDateTime.now()).orElse(0l));
        }
        OverdueStatus[] statuses = OverdueStatus.values();
        for (OverdueStatus s : statuses) {
            map.put(s.name(), ticketRepository.countByOverdueAndAssigneeGroupByOverdue(userId, s, getTimeForInterval(time), LocalDateTime.now()).orElse(0l));
        }
        return map;
    }

    public Map<String, Long> getDepartmentTicketsByStatus(Long departmentId, TimeInterval time) {
        Map<String, Long> map = new HashMap<>();
        Status[] enums = Status.values();
        for (Status s : enums) {
            map.put(s.name(), ticketRepository.countByStatusAndDepartmentGroupByStatus(departmentId, s, getTimeForInterval(time), LocalDateTime.now()).orElse(0l));
        }
        OverdueStatus[] statuses = OverdueStatus.values();
        for (OverdueStatus s : statuses) {
            map.put(s.name(), ticketRepository.countByOverdueAndDepartmentGroupByOverdue(departmentId, s, getTimeForInterval(time), LocalDateTime.now()).orElse(0l));
        }
        return map;
    }

    public Long getTicketsCountByAssignee(UUID userId, TimeInterval time) {
        return ticketRepository.countByAssigneeAndDueDateBetween(userId, getTimeForInterval(time), LocalDateTime.now()).orElse(0l);
    }

    public Long getTicketsCountByDepartment(Long departmentId, TimeInterval time) {
        return ticketRepository.countByDepartmentAndDueDateBetween(departmentId, getTimeForInterval(time), LocalDateTime.now()).orElse(0l);
    }
}
