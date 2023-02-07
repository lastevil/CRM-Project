package org.unicrm.analytic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.analytic.dto.CurrentInformation;
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
            if (userDto.getDepartmentId() != null) {
                facade.getDepartmentRepository().save(department);
            }
            if (userDto.getId() != null) {
                facade.getUserRepository().save(user);
            }
        }
    }

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

    public Page<TicketFrontDto> getTicketByAssignee(CurrentInformation information) {
        Pageable pageable = PageRequest
                .of(information.getPage() - 1, information.getCountElements(), Sort.by("createdAt"));
        //ToDo: проверка на интервал
        return facade.getTicketRepository()
                .findAllByAssigneeIdWithStatus(pageable, information.getUserId(), information.getStatus().getValue())
                .map(ticket -> facade.getTicketMapper().fromEntityToFrontDto(ticket));
    }

    public Page<TicketFrontDto> getTicketByAssigneeDepartment(CurrentInformation information) {
        Pageable pageable = PageRequest.of(information.getPage() - 1, information.getCountElements(), Sort.by("createdAt"));
        //ToDo: проверка на интервал
        return facade.getTicketRepository()
                .findAllByAssigneeDepartmentWithStatus(pageable, information.getDepartmentId(), information.getStatus().getValue())
                .map(ticket -> facade.getTicketMapper().fromEntityToFrontDto(ticket));
    }

    public Page<UserFrontDto> getUsersFromDepartment(CurrentInformation information) {
        Pageable pageable = PageRequest.of(information.getPage() - 1, information.getCountElements(), Sort.by("lastName"));
        return facade.getUserRepository().findAllByDepartmentId(pageable, information.getDepartmentId())
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
                .orElseThrow(() -> new ResourceNotFoundException("Департамент с id " + userDto.getDepartmentTitle() + " не найден"));
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