package org.unicrm.ticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.lib.dto.UserDto;
import org.unicrm.ticket.dto.TicketDto;
import org.unicrm.ticket.dto.TicketUserDto;
import org.unicrm.ticket.entity.Ticket;
import org.unicrm.ticket.entity.TicketDepartment;
import org.unicrm.ticket.entity.TicketStatus;
import org.unicrm.ticket.entity.TicketUser;
import org.unicrm.ticket.exception.ResourceNotFoundException;
import org.unicrm.ticket.services.utils.TicketFacade;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TicketService {

    private final KafkaTemplate<UUID, TicketDto> kafkaTemplate;

    private final TicketFacade facade;
    private final TicketDepartmentService departmentService;
    private final TicketUserService userService;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

//    @KafkaListener(topics = "userTopic", containerFactory = "userKafkaListenerContainerFactory")
//    @Transactional
//    public void createOrUpdateUserAndDepartment(UserDto userDto) {
//        TicketDepartment department = departmentSaveOrUpdate(userDto);
//        userSaveOrUpdate(userDto, department);
//    }

    private TicketDepartment departmentSaveOrUpdate(UserDto dto) {
        TicketDepartment department;
        if (!facade.getDepartmentRepository().existsById(dto.getDepartmentId())) {
            department = facade.getDepartmentMapper().toEntity(dto);
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

//    private void userSaveOrUpdate(UserDto dto, TicketDepartment department) {
//        TicketUser user;
//        if (!facade.getUserRepository().existsById(dto.getId())) {
//            user = facade.getUserMapper().toEntity(dto, department);
//        } else {
//            user = facade.getUserRepository().findById(dto.getId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + dto.getId() + "не найден в базе"));
//            user.setDepartment(department);
//            if (dto.getFirstName() != null) {
//                user.setFirstName(dto.getFirstName());
//            }
//            if (dto.getLastName() != null) {
//                user.setLastName(user.getLastName());
//            }
//        }
//        facade.getUserRepository().save(user);
//    }

    public List<TicketDto> findAll() {
        return facade.getTicketRepository().findAll().stream().map(facade.getTicketMapper()::toDto).collect(Collectors.toList());
    }

    public TicketDto findTicketById(UUID id) {
        Ticket ticket = facade.getTicketRepository().findById(id).orElse(null);
        return facade.getTicketMapper().toDto(ticket);
    }

    @Transactional
    public void deleteById(UUID id) {
        facade.getTicketRepository().deleteById(id);
    }

    @Transactional
    public Ticket createTicket(TicketDto ticketDto) {
        LocalDateTime creationTime = LocalDateTime.now();
        TicketUser assignee = userService.findUserById(ticketDto.getAssigneeId().getId());
        TicketUser reporter = userService.findUserById(ticketDto.getReporterId().getId());
        Ticket ticket = facade.getTicketMapper().toEntity(ticketDto, ticketDto.getAssigneeId(), ticketDto.getReporterId(), ticketDto.getDepartmentId());
        ticket.setCreatedAt(creationTime);
        ticket.setStatus(TicketStatus.BACKLOG);
        return facade.getTicketRepository().save(ticket);
    }

    @Transactional
    public void update(TicketDto ticketDto) {
        Date today = new Date(System.currentTimeMillis());
        Ticket ticket = facade.getTicketRepository().findById(ticketDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to update ticket. Ticket not found, id: " + ticketDto.getId()));
        ticket.setTitle(ticketDto.getTitle());
        ticket.setStatus(ticketDto.getStatus());
        ticket.setDescription(ticketDto.getDescription());
//        ticket.setAssigneeId(ticketDto.getAssigneeId());
//        ticket.setReporterId(ticketDto.getReporterId());
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setDueDate(ticketDto.getDueDate());
        ticket.setIsOverdue(today.after(ticketDto.getDueDate()));
        kafkaTemplate.send("ticketTopic", UUID.randomUUID(), facade.getTicketMapper().toDto(ticket));
    }

//    public List<TicketDto> findTicketsByAssignee(UserDto userDto) {
//        return facade.getTicketRepository().findAllByAssignee(facade.getUserMapper().toEntity(userDto, departmentService.findDepartmentById(userDto)))
//                .stream().map(facade.getTicketMapper()::toDto)
//                .collect(Collectors.toList());
//    }

    public List<TicketDto> findTicketsByDepartment(TicketDepartment ticketDepartment) {
        return facade.getTicketRepository().findAllByDepartment(ticketDepartment.getDepartmentId())
                .stream().map(facade.getTicketMapper()::toDto)
                .collect(Collectors.toList());
    }

    public List<TicketDto> findTicketsByAssigneeAndStatus(TicketUserDto assignee, String status) {
        return facade.getTicketRepository().findAllByAssigneeIdAndStatus(assignee.getId(), status)
                .stream().map(facade.getTicketMapper()::toDto)
                .collect(Collectors.toList());
    }
}
