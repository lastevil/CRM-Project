package org.unicrm.analytic.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.analytic.api.Status;
import org.unicrm.analytic.dto.CurrentInformation;
import org.unicrm.analytic.dto.DepartmentFrontDto;
import org.unicrm.analytic.dto.TicketFrontDto;
import org.unicrm.analytic.dto.UserFrontDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;
import org.unicrm.analytic.exceptions.InvalidKafkaDtoException;
import org.unicrm.analytic.exceptions.ResourceNotFoundException;
import org.unicrm.analytic.services.utils.AnalyticFacade;
import org.unicrm.lib.dto.TicketDto;
import org.unicrm.lib.dto.UserDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j
public class AnalyticService {
    private final AnalyticFacade facade;

    @KafkaListener(topics = "userTopic", containerFactory = "userKafkaListenerContainerFactory")
    @Transactional
    void createOrUpdateUserAndDepartment(UserDto userDto) {
        if (userDto.getId() == null) {
            throw new InvalidKafkaDtoException("Wrong data");
        } else {
            Department department = departmentSaveOrUpdate(userDto);
            userSaveOrUpdate(userDto, department);
        }
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

    @KafkaListener(topics = "ticketTopic", containerFactory = "ticketKafkaListenerContainerFactory")
    @Transactional
    void createOrUpdateTicket(TicketDto ticketDto) {
        Ticket ticket;
        if (facade.getTicketRepository().existsById(ticketDto.getId())) {
            if(ticketDto.getStatus().equals(Status.DELETED.getValue())){
                facade.getTicketRepository().deleteById(ticketDto.getId());
            }
            ticket = updateTicket(ticketDto);
        } else {

            ticket = createTicket(ticketDto);
        }
        facade.getTicketRepository().save(ticket);
    }

    private Ticket updateTicket(TicketDto ticketDto) {
        Ticket ticket = facade.getTicketRepository().findById(ticketDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Задача с id " + ticketDto.getId() + " не найдена"));
        ticket.setUpdatedAt(ticketDto.getUpdatedAt());
        ticket.setStatus(ticketDto.getStatus());
        if (ticketDto.getAssigneeId() != null) {
            ticket.setAssignee(facade.getUserRepository().findById(ticketDto.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id: " + ticketDto.getAssigneeId() + " не найден")));
        }
        return ticket;
    }

    private Ticket createTicket(TicketDto ticketDto) {
        if (ticketDto.getAssigneeId() != null && ticketDto.getAssigneeDepartmentId() != null) {
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
        } else
            throw new InvalidKafkaDtoException("Wrong data");
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

    public UserFrontDto getUser(UUID id) {
        return facade.getUserMapper().fromEntityToFrontDto(
                facade.getUserRepository().findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден"))
        );
    }
}