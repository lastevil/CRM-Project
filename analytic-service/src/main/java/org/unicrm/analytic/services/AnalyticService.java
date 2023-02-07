package org.unicrm.analytic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.analytic.dto.DepartmentFrontDto;
import org.unicrm.analytic.dto.TicketFrontDto;
import org.unicrm.analytic.dto.UserFrontDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;
import org.unicrm.analytic.exceptions.ResourceNotFoundException;
import org.unicrm.analytic.services.utils.AnalyticFacade;
import org.unicrm.lib.dto.TicketDto;
import org.unicrm.lib.dto.UserDto;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticService {
    private final AnalyticFacade facade;

    @KafkaListener(topics = "userTopic", containerFactory = "userKafkaListenerContainerFactory")
    @Transactional
    void createUserAndDepartment(UserDto userDto) {
        if (userDto.getId() != null) {
            Department department = facade.getDepartmentMapper().fromUserDto(userDto);
            User user = facade.getUserMapper().fromUserDto(userDto, department);
            if (!facade.getDepartmentRepository().existsById(department.getId())) {
                facade.getDepartmentRepository().save(department);
            }
            if (!facade.getUserRepository().existsById(user.getId())) {
                facade.getUserRepository().save(user);
            }
        }
    }

    //ToDo: добавить сбор перечисления EnumSet для статусов и интегрировать их в взаимодействие.
    @KafkaListener(topics = "ticketTopic", containerFactory = "ticketKafkaListenerContainerFactory")
    @Transactional
    void createTicket(TicketDto ticketDto) {
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
        Ticket ticket = facade.getTicketMapper().fromTicketDto(ticketDto, reporter, assignee, department);
        facade.getTicketRepository().save(ticket);
    }

    public Page<TicketFrontDto> getTicketByAssignee(UUID id, String status, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("createdAt"));
        return facade.getTicketRepository().findAllByAssigneeIdWithStatus(pageable, id, status)
                .map(ticket -> facade.getTicketMapper().fromEntityToFrontDto(ticket));
    }

    public Page<TicketFrontDto> getTicketByAssigneeDepartment(UUID id, String status, int page, int count) {
        Pageable pageable = PageRequest.of(page - 1, count, Sort.by("createdAt"));
        return facade.getTicketRepository().findAllByAssigneeDepartmentWithStatus(pageable, id, status)
                .map(ticket -> facade.getTicketMapper().fromEntityToFrontDto(ticket));
    }

    public Page<UserFrontDto> getUsersFromDepartment(Long id, int page, int count) {
        Pageable pageable = PageRequest.of(page - 1, count, Sort.by("lastName"));
        return facade.getUserRepository().findAllByDepartmentId(pageable, id)
                .map(u -> facade.getUserMapper().fromEntityToFrontDto(u));
    }

    public List<DepartmentFrontDto> getDepartments() {
        return facade.getDepartmentRepository().findAll().stream()
                .map(d -> facade.getDepartmentMapper().fromEntityToFrontDto(d))
                .collect(Collectors.toList());
    }

    //возможно стоит перевести с rest на kafka.
    @Transactional
    public void changeUserDepartment(UserDto userDto) {
        User user = facade.getUserRepository().findById(userDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + userDto.getId() + " не найден"));
        Department department = facade.getDepartmentRepository().findById(userDto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Департамент с id " + userDto.getDepartmentId() + " не найден"));
        user.setDepartment(department);
        facade.getUserRepository().save(user);
    }

    public UserFrontDto getUser(UUID id) {
        return facade.getUserMapper().fromEntityToFrontDto(
                facade.getUserRepository().findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден"))
        );
    }

    //Аналогично обновлению информации о пользователе возможно логичнее через kafka
    @Transactional
    public void updateTicketStatus(TicketDto ticketDto) {
        Ticket ticket = facade.getTicketRepository().findById(ticketDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + ticketDto.getId() + " не найдена"));
        ticket.setStatus(ticketDto.getStatus());
        ticket.setUpdatedAt(ticketDto.getUpdatedAt());
        facade.getTicketRepository().save(ticket);
    }
}