package org.unicrm.ticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.lib.dto.UserDto;
import org.unicrm.ticket.dto.TicketDto;
import org.unicrm.ticket.dto.TicketRequestDto;
import org.unicrm.ticket.dto.TicketResponseDto;
import org.unicrm.ticket.entity.Ticket;
import org.unicrm.ticket.entity.TicketDepartment;
import org.unicrm.ticket.entity.TicketStatus;
import org.unicrm.ticket.entity.TicketUser;
import org.unicrm.ticket.exception.ResourceNotFoundException;
import org.unicrm.ticket.mapper.TicketDepartmentMapper;
import org.unicrm.ticket.mapper.TicketUserMapper;
import org.unicrm.ticket.services.utils.TicketFacade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TicketService {

    private final KafkaTemplate<UUID, TicketDto> kafkaTemplate;

    private final TicketFacade facade;
    private final TicketDepartmentService departmentService;
    private final TicketUserService userService;
    private final TicketUserMapper ticketUserMapper;
    private final TicketDepartmentMapper ticketDepartmentMapper;

    @KafkaListener(topics = "userTopic", containerFactory = "userKafkaListenerContainerFactory")
    @Transactional
    public void createOrUpdateUserAndDepartment(UserDto userDto) {
        TicketDepartment department = departmentSaveOrUpdate(userDto);
        userSaveOrUpdate(userDto, department);
    }

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

    private void userSaveOrUpdate(UserDto dto, TicketDepartment department) {
        TicketUser user;
        if (!facade.getUserRepository().existsById(dto.getId())) {
            user = facade.getUserMapper().tofromGlobalDto(dto, department);
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

    public List<TicketResponseDto> findTicketsByAssignee(UUID assignee) {
        return facade.getTicketRepository()
                .findAllByAssignee(assignee)
                        .stream().map(facade.getTicketMapper()::toResponseDtoFromEntity)
                        .collect(Collectors.toList());
    }

    public List<TicketResponseDto> findTicketsByDepartment(Long ticketDepartment) {
        return facade.getTicketRepository().findAllByDepartment(ticketDepartment)
                .stream().map(facade.getTicketMapper()::toResponseDtoFromEntity)
                .collect(Collectors.toList());
    }

    public List<TicketResponseDto> findTicketsByAssigneeAndStatus(UUID assignee, String status) {
        return facade.getTicketRepository().findAllByAssigneeIdAndStatus(assignee, TicketStatus.valueOf(status))
                .stream().map(facade.getTicketMapper()::toResponseDtoFromEntity)
                .collect(Collectors.toList());
    }

    public List<TicketResponseDto> findTicketByTitle(String title) {
        return facade.getTicketRepository().findTicketsByTitle(title)
                .stream().map(facade.getTicketMapper()::toResponseDtoFromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createTicket(TicketRequestDto ticketDto, Long departmentId, UUID assigneeId, String username) {
        TicketUser assignee = userService.findUserById(assigneeId);
        TicketUser reporter = userService.findUserByUsername(username);
        TicketDepartment department = departmentService.findDepartmentById(departmentId);
        Ticket ticket = facade.getTicketMapper().toEntityFromTicketRequest(ticketDto, assignee, reporter, department);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setStatus(TicketStatus.BACKLOG);
        facade.getTicketRepository().save(ticket);
        kafkaTemplate.send("ticketTopic", UUID.randomUUID(), facade.getTicketMapper().toDto(ticket));
    }

    @Transactional
    public void update(TicketRequestDto ticketDto, UUID id, Long departmentId, UUID assigneeId) {
        Ticket ticket = facade.getTicketRepository().findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (departmentId != null) {
            TicketDepartment department = facade.getDepartmentRepository().findById(departmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            ticket.setDepartment(department);
        }
        if (assigneeId != null) {
            TicketUser user = facade.getUserRepository().findById(assigneeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        }
        if (ticketDto.getTitle() != null) {
            ticket.setTitle(ticketDto.getTitle());
        }
        if (ticketDto.getDescription() != null) {
            ticket.setDescription(ticketDto.getDescription());
        }
        if (ticketDto.getDueDate() != null) {
            ticket.setDueDate(LocalDateTime.of(ticketDto.getDueDate(), LocalTime.of(21, 0, 0)));
        }
        if(ticketDto.getStatus().equals(TicketStatus.DONE) || ticketDto.getStatus().equals(TicketStatus.ACCEPTED)) {
            if(!ticket.getOverdue().equals(TicketStatus.OVERDUE))
                ticket.setOverdue(null);
        }
        kafkaTemplate.send("ticketTopic", UUID.randomUUID(), facade.getTicketMapper().toDto(ticket));
    }

    @Scheduled(initialDelay = 1, fixedDelay = 120, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void updateDueStatus() {
        List<Ticket> ticketList = facade.getTicketRepository()
                .findAllWithStatuses(TicketStatus.BACKLOG, TicketStatus.IN_PROGRESS, LocalDateTime.now().plusDays(1), LocalDateTime.now().minusDays(4));
        ticketList.stream().forEach(t -> t.setOverdue(null));
        ticketList.stream().filter(t -> t.getDueDate().minusDays(4).toLocalDate().isBefore(LocalDate.now()))
                .forEach(t -> {
                    t.setOverdue(TicketStatus.THREE_DAYS_LEFT);
                    kafkaTemplate.send("ticketTopic", UUID.randomUUID(), facade.getTicketMapper().toDto(t));
                });
        ticketList.stream().filter(t -> t.getDueDate().minusDays(3).toLocalDate().isBefore(LocalDate.now()))
                .forEach(t -> t.setOverdue(TicketStatus.TWO_DAYS_LEFT));
        ticketList.stream().filter(t -> t.getDueDate().minusDays(2).toLocalDate().isBefore(LocalDate.now()))
                .forEach(t -> t.setOverdue(TicketStatus.TODAY_LEFT));
        ticketList.stream().filter(t -> t.getDueDate().minusDays(1).toLocalDate().isBefore(LocalDate.now()))
                .forEach(t -> t.setOverdue(TicketStatus.OVERDUE));
    }
}
