package org.unicrm.analytic.services;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.analytic.api.Status;
import org.unicrm.analytic.api.TimeInterval;
import org.unicrm.analytic.converter.TicketMapper;
import org.unicrm.analytic.dto.TicketResponseDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;
import org.unicrm.analytic.exceptions.ResourceNotFoundException;
import org.unicrm.analytic.repositorys.TicketRepository;
import org.unicrm.lib.dto.TicketDto;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
            } else {
                updateTicket(ticketDto);
            }
        } else {
            if (ticketDto.getAssigneeDepartmentId() != null) {
                ticket = createTicket(ticketDto);
                ticketRepository.save(ticket);
            }
        }
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

    private Ticket updateTicket(TicketDto ticketDto) {
        Ticket ticket = ticketRepository.findById(ticketDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + ticketDto.getId() + " не найдена"));
        ticket.setUpdatedAt(ticketDto.getUpdatedAt());
        if (ticketDto.getOverdue() != null) {
            ticket.setOverdue(Status.valueOf(ticketDto.getOverdue()));
        }
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

    public Map<Status, Integer> getAssigneeTicketsByStatus(UUID userId, TimeInterval time) {
        return ticketRepository.countByStatusDueDateBetweenAndAssignee(userId, getTimeForInterval(time), LocalDateTime.now());
    }

    public Map<Status, Integer> getAssigneeTicketsByOverdue(UUID userId, TimeInterval time) {
        return ticketRepository.countByOverdueDueDateBetweenAndAssignee(userId, getTimeForInterval(time), LocalDateTime.now());
    }

    public Map<Status, Integer> getDepartmentTicketsByStatus(Long departmentId, TimeInterval time) {
        return ticketRepository.countByStatusDueDateBetweenDepAndDepartment(departmentId, getTimeForInterval(time), LocalDateTime.now());
    }

    public Map<Status, Integer> getDepartmentTicketsByOverdue(Long departmentId, TimeInterval time) {
        return ticketRepository.countByOverdueDueDateBetweenAndDepartment(departmentId, getTimeForInterval(time), LocalDateTime.now());
    }

    public Integer getTicketsCountByAssignee(UUID userId, TimeInterval time) {
        return ticketRepository.countByAssigneeAndDueDateBetween(userId, getTimeForInterval(time), LocalDateTime.now());
    }

    public Integer getTicketsCountByDepartment(Long departmentId, TimeInterval time) {
        return ticketRepository.countByDepartmentAndDueDateBetween(departmentId, getTimeForInterval(time), LocalDateTime.now());
    }
}
