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
import org.unicrm.analytic.dto.*;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;
import org.unicrm.analytic.exceptions.ResourceNotFoundException;
import org.unicrm.analytic.services.utils.AnalyticFacade;
import org.unicrm.lib.dto.TicketDto;
import org.unicrm.lib.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticService {
    private final AnalyticFacade facade;

    @KafkaListener(topics = "userTopic", containerFactory = "userKafkaListenerContainerFactory")
    @Transactional
    public void createOrUpdateUserAndDepartment(UserDto userDto) {
        Department department = departmentSaveOrUpdate(userDto);
        userSaveOrUpdate(userDto, department);

    }


    @KafkaListener(topics = "ticketTopic", containerFactory = "ticketKafkaListenerContainerFactory")
    @Transactional
    public void createOrUpdateTicket(TicketDto ticketDto) {
        Ticket ticket;
        if (facade.getTicketRepository().existsById(ticketDto.getId())) {
            if (ticketDto.getStatus().equals(Status.DELETED.getValue())) {
                facade.getTicketRepository().deleteById(ticketDto.getId());
            }
            ticket = updateTicket(ticketDto);
        } else {
            if (ticketDto.getAssigneeId() != null && ticketDto.getAssigneeDepartmentId() != null) {
                ticket = createTicket(ticketDto);
            } else return;
        }
        facade.getTicketRepository().save(ticket);
    }

    public Page<TicketFrontDto> getTicketByAssignee(UUID userId, Status status, CurrentPage currentPage) {
        Pageable pageable = PageRequest
                .of(currentPage.getPage() - 1, currentPage.getCountElements(), Sort.by("createdAt"));
        LocalDateTime between = getTimeForInterval(currentPage.getTimeInterval());
        return facade.getTicketRepository()
                .findAllByAssigneeIdWithStatus(pageable, userId, status, between, LocalDateTime.now())
                .map(ticket -> facade.getTicketMapper().fromEntityToFrontDto(ticket));
    }

    public Page<TicketFrontDto> getTicketByAssigneeDepartment(Long departmentId, Status status, CurrentPage currentPage) {
        Pageable pageable = PageRequest.of(currentPage.getPage() - 1, currentPage.getCountElements(), Sort.by("createdAt"));
        LocalDateTime between = getTimeForInterval(currentPage.getTimeInterval());
        return facade.getTicketRepository()
                .countByAssigneeDepartmentWithStatus(pageable, departmentId, status, between, LocalDateTime.now())
                .map(ticket -> facade.getTicketMapper().fromEntityToFrontDto(ticket));
    }

    public Page<UserFrontDto> getUsersFromDepartment(Long departmentId, CurrentPage currentPage) {
        Pageable pageable = PageRequest.of(currentPage.getPage() - 1, currentPage.getCountElements(), Sort.by("lastName"));
        return facade.getUserRepository().findAllByDepartmentId(pageable, departmentId)
                .map(u -> facade.getUserMapper().fromEntityToFrontDto(u));
    }

    public List<DepartmentFrontDto> getDepartments() {
        return facade.getDepartmentRepository().findAll().stream()
                .map(d -> facade.getDepartmentMapper().fromEntityToFrontDto(d))
                .collect(Collectors.toList());
    }

    private LocalDateTime getTimeForInterval(TimeInterval interval) {
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

    @Transactional
    public GlobalInfo getUserInfo(UUID userId, TimeInterval time) {
        return GlobalInfo.builder()
                .ticketCountDone(facade.getTicketRepository()
                        .countByAssigneeIdWithStatus(userId, Status.DONE, getTimeForInterval(time), LocalDateTime.now()))
                .ticketCountInProgress(facade.getTicketRepository()
                        .countByAssigneeIdWithStatus(userId, Status.IN_PROGRESS, getTimeForInterval(time), LocalDateTime.now()))
                .ticketCountAccepted(facade.getTicketRepository()
                        .countByAssigneeIdWithStatus(userId, Status.ACCEPTED, getTimeForInterval(time), LocalDateTime.now()))
                .ticketCountOverdue(facade.getTicketRepository()
                        .countByAssigneeIdWithStatus(userId, Status.OVERDUE, getTimeForInterval(time), LocalDateTime.now()))
                .build();
    }

    public GlobalInfo getDepartmentInfo(Long departmentId, TimeInterval beginTime) {
        return GlobalInfo.builder()
                .ticketCountDone(facade.getTicketRepository()
                        .countByAssigneeDepartmentWithStatus(departmentId, Status.DONE, getTimeForInterval(beginTime), LocalDateTime.now()))
                .ticketCountInProgress(facade.getTicketRepository()
                        .countByAssigneeDepartmentWithStatus(departmentId, Status.IN_PROGRESS, getTimeForInterval(beginTime), LocalDateTime.now()))
                .ticketCountAccepted(facade.getTicketRepository()
                        .countByAssigneeDepartmentWithStatus(departmentId, Status.ACCEPTED, getTimeForInterval(beginTime), LocalDateTime.now()))
                .ticketCountOverdue(facade.getTicketRepository()
                        .countByAssigneeDepartmentWithStatus(departmentId, Status.OVERDUE, getTimeForInterval(beginTime), LocalDateTime.now()))
                .build();
    }

    private Department departmentSaveOrUpdate(UserDto dto) {
        Department department;
        if (!facade.getDepartmentRepository().existsById(dto.getDepartmentId())) {
            department = facade.getDepartmentMapper().fromUserDto(dto);
            facade.getDepartmentRepository().save(department);
        } else {
            department = facade.getDepartmentRepository().findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Департамент исполнителя не найден в базе"));
            if (dto.getDepartmentTitle() != null) {
                department.setTitle(dto.getDepartmentTitle());
                facade.getDepartmentRepository().save(department);
            }
        }
        return department;
    }

    private void userSaveOrUpdate(UserDto dto, Department department) {
        User user;
        if (!facade.getUserRepository().existsById(dto.getId())) {
            user = facade.getUserMapper().fromUserDto(dto, department);
        } else {
            user = facade.getUserRepository().findById(dto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + dto.getId() + "не найден в базе"));
            user.setDepartment(department);
            if (dto.getFirstName() != null) {
                user.setFirstName(dto.getFirstName());
            }
            if (dto.getLastName() != null) {
                user.setLastName(user.getLastName());
            }
        }
        facade.getUserRepository().save(user);
    }

    private Ticket updateTicket(TicketDto ticketDto) {
        Ticket ticket = facade.getTicketRepository().findById(ticketDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + ticketDto.getId() + " не найдена"));
        ticket.setUpdatedAt(ticketDto.getUpdatedAt());
        ticket.setStatus(Status.valueOf(ticketDto.getStatus()));
        if (ticketDto.getAssigneeId() != null) {
            ticket.setAssignee(facade.getUserRepository().findById(ticketDto.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id: " + ticketDto.getAssigneeId() + " не найден")));
        }
        return ticket;
    }

    private Ticket createTicket(TicketDto ticketDto) {
        Department department = facade.getDepartmentRepository().findById(ticketDto.getAssigneeDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Департамент исполнителя не найден в базе"));
        User reporter = facade.getUserRepository().findById(ticketDto.getReporterId())
                .orElseThrow(() -> new ResourceNotFoundException("Заявитель не найден в базе"));
        User assignee;
        if (ticketDto.getAssigneeId() != null) {
            assignee = facade.getUserRepository().findById(ticketDto.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Исполнитель не найден в базе"));
        } else {
            assignee = null;
        }
        return facade.getTicketMapper().fromTicketDto(ticketDto, reporter, assignee, department);
    }
}