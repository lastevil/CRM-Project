package org.unicrm.ticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.ticket.dto.TicketPage;
import org.unicrm.ticket.dto.TicketRequestDto;
import org.unicrm.ticket.dto.TicketResponseDto;
import org.unicrm.ticket.dto.kafka.KafkaTicketDto;
import org.unicrm.ticket.dto.kafka.KafkaUserDto;
import org.unicrm.ticket.entity.Ticket;
import org.unicrm.ticket.entity.Department;
import org.unicrm.ticket.entity.TicketStatus;
import org.unicrm.ticket.entity.User;
import org.unicrm.ticket.exception.NoPermissionToChangeException;
import org.unicrm.ticket.exception.NoPermissionToDeleteException;
import org.unicrm.ticket.exception.ResourceNotFoundException;
import org.unicrm.ticket.exception.validators.*;
import org.unicrm.ticket.repository.TicketRepository;
import org.unicrm.ticket.services.utils.TicketFacade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final KafkaTemplate<UUID, KafkaTicketDto> kafkaTemplate;
    private final TicketFacade facade;
    private final TicketDepartmentService departmentService;
    private final TicketUserService userService;
    private final TicketRepository ticketRepository;
    private final TicketValidator ticketValidator;
    private final TicketUserValidator userValidator;
    private final TicketDepartmentValidator departmentValidator;
    private final TicketPageValidator pageValidator;

    private static final String TICKET_TOPIC = "ticketTopic";

    @KafkaListener(topics = "userTopic", containerFactory = "userKafkaListenerContainerFactory")
    @Transactional
    public void createOrUpdateUserAndDepartment(KafkaUserDto userDto) {
        departmentValidator.validate(userDto);
        userValidator.validate(userDto);
        Department department = departmentSaveOrUpdate(userDto);
        userSaveOrUpdate(userDto, department);
    }

    private Department departmentSaveOrUpdate(KafkaUserDto dto) {
        Department department;
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

    private void userSaveOrUpdate(KafkaUserDto dto, Department department) {
        User user;
        if (!facade.getUserRepository().existsById(dto.getId())) {
            user = facade.getUserMapper().toEntityfromGlobalDto(dto, department);
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

    public Page<TicketResponseDto> findAll(TicketPage index) {
        if (index.getPage() < 1) {
            index.setPage(1);
        }
        if (index.getSize() < 1) {
            index.setSize(10);
        }
        Pageable pageable = PageRequest.of(index.getPage() - 1, index.getSize(), Sort.by("updatedAt").descending());
        return facade.getTicketRepository().findAllTickets(pageable)
                .map(ticket -> facade.getTicketMapper().toResponseDtoFromEntity(ticket));
    }

    public Page<TicketResponseDto> findTicketsByAssignee(String role, UUID assignee, TicketPage index) {
        pageValidator.validate(index);
        if(role.equals("ROLE_USER")) {
            throw new NoPermissionToView("You have insufficient privileges to perform this action");
        } else {
        Pageable pageable = PageRequest.of(index.getPage() - 1, index.getSize(), Sort.by("updatedAt").descending());
        return facade.getTicketRepository()
                .findAllByAssignee(pageable, assignee)
                .map(ticket -> facade.getTicketMapper().toResponseDtoFromEntity(ticket));
    }
    }

    public Page<TicketResponseDto> findTicketsByDepartment(TicketPage index, Long ticketDepartment) {
        if (index.getPage() < 1) {
            index.setPage(1);
        }
        if (index.getSize() < 1) {
            index.setSize(10);
        }
        Pageable pageable = PageRequest.of(index.getPage() - 1, index.getSize(), Sort.by("updatedAt").descending());
        return facade.getTicketRepository().findAllByDepartment(pageable, ticketDepartment)
                .map(ticket -> facade.getTicketMapper().toResponseDtoFromEntity(ticket));
    }

    public Page<TicketResponseDto> findTicketsByAssigneeAndStatus(String role, UUID assignee, String status, TicketPage index) {
        if(role.equals("ROLE_USER")) {
            throw new NoPermissionToView("You have insufficient privileges to perform this action");
        }
        else {
            pageValidator.validate(index);
            Pageable pageable = PageRequest.of(index.getPage() - 1, index.getSize(),
                    Sort.by("updatedAt").descending());
            return facade.getTicketRepository().findAllByAssigneeIdAndStatus(pageable, assignee, TicketStatus.valueOf(status))
                    .map(ticket -> facade.getTicketMapper().toResponseDtoFromEntity(ticket));
        }
    }

    public Page<TicketResponseDto> findTicketByTitle(String role, TicketPage index, String title) {
        if(role.equals("ROLE_USER")) {
            throw new NoPermissionToView("You have insufficient privileges to perform this action");
        }
        else {
            pageValidator.validate(index);
            Pageable pageable = PageRequest.of(index.getPage() - 1, index.getSize());
            return facade.getTicketRepository().findTicketsByTitle(pageable, title)
                    .map(ticket -> facade.getTicketMapper().toResponseDtoFromEntity(ticket));
        }
    }

    public Page<TicketResponseDto> findTicketByStatus(TicketPage index, String status) {
        if (index.getPage() < 1) {
            index.setPage(1);
        }
        if (index.getSize() < 1) {
            index.setSize(10);
        }
        Pageable pageable = PageRequest.of(index.getPage() - 1, index.getSize(), Sort.by("updatedAt").descending());
        return facade.getTicketRepository().findTicketsByStatus(pageable, TicketStatus.valueOf(status))
                .map(ticket -> facade.getTicketMapper().toResponseDtoFromEntity(ticket));
    }

    public Page<TicketResponseDto> findAllByReporter(String role, TicketPage index, String reporter) {
        if(role.equals("ROLE_USER")) {
            throw new NoPermissionToView("You have insufficient privileges to perform this action");
        } else {
            pageValidator.validate(index);
            Pageable pageable = PageRequest.of(index.getPage() - 1, index.getSize(), Sort.by("updatedAt").descending());
            return facade.getTicketRepository().findTicketsByReporter(pageable, reporter)
                    .map(ticket -> facade.getTicketMapper().toResponseDtoFromEntity(ticket));
        }
    }

    public TicketResponseDto findTicketById(UUID id) {
        Ticket ticket = facade.getTicketRepository().findById(id).orElse(null);
        return facade.getTicketMapper().toResponseDtoFromEntity(ticket);
    }

    @Transactional
    public void deleteById(UUID id, String username, String role) {
        if (role.equals("ROLE_ADMIN")) {
            Ticket ticket = facade.getTicketRepository().findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
            User reporter = userService.findUserByUsername(username);
            if (ticket.getStatus().equals(TicketStatus.BACKLOG) && ticket.getReporter().equals(reporter)) {
                ticket.setStatus(TicketStatus.DELETED);
                kafkaTemplate.send(TICKET_TOPIC, UUID.randomUUID(), facade.getTicketMapper().toDto(ticket));
                facade.getTicketRepository().deleteById(id);
            }
        } else {
            throw new NoPermissionToDeleteException("You don't have sufficient privileges to delete this ticket");
        }

    }

    @Transactional
    public void createTicket(TicketRequestDto ticketDto, Long departmentId, UUID assigneeId, String username) {
        ticketValidator.validateCreation(ticketDto, departmentId, assigneeId, username);
        User assignee = userService.findUserById(assigneeId);
        User reporter = userService.findUserByUsername(username);
        Department department = departmentService.findDepartmentById(departmentId);
        Ticket ticket = facade.getTicketMapper().toEntityFromTicketRequest(ticketDto, assignee, reporter, department);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setStatus(TicketStatus.BACKLOG);
        facade.getTicketRepository().save(ticket);
        kafkaTemplate.send(TICKET_TOPIC, UUID.randomUUID(), facade.getTicketMapper().toDto(ticket));
    }


    //TODO: Refactor
    @Transactional
    public void update(TicketRequestDto ticketDto, UUID id, Long departmentId, UUID assigneeId, String username, String role) {
        Ticket ticket = facade.getTicketRepository().findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        User user = facade.getUserRepository().findByUsername(username);
        TicketStatus status = ticket.getStatus();
        if (role.equals("ROLE_ADMIN")) {
            prepareUpdate(ticket, departmentId, assigneeId, ticketDto);
            kafkaTemplate.send(TICKET_TOPIC, UUID.randomUUID(), facade.getTicketMapper().toDto(ticket));
        } else if (user.equals(ticket.getAssignee()) && !status.equals(TicketStatus.ACCEPTED) && !status.equals(TicketStatus.DELETED)) {
            prepareUpdate(ticket, departmentId, assigneeId, ticketDto);
            kafkaTemplate.send(TICKET_TOPIC, UUID.randomUUID(), facade.getTicketMapper().toDto(ticket));
        } else if (user.equals(ticket.getReporter()) && status.equals(TicketStatus.BACKLOG)) {
            prepareUpdate(ticket, departmentId, assigneeId, ticketDto);
            kafkaTemplate.send(TICKET_TOPIC, UUID.randomUUID(), facade.getTicketMapper().toDto(ticket));
        } else {
            throw new NoPermissionToChangeException("Unable to update the task with id: " + id);
        }
    }

    //TODO: Refactor
    public void prepareUpdate(Ticket ticket, Long departmentId, UUID assigneeId, TicketRequestDto ticketRequestDto) {
        if (departmentId != null) {
            Department department = facade.getDepartmentRepository().findById(departmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            ticket.setDepartment(department);
        }
        if (assigneeId != null) {
            User user = facade.getUserRepository().findById(assigneeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            ticket.setAssignee(user);
        }
        if (ticketRequestDto.getTitle() != null) {
            ticket.setTitle(ticketRequestDto.getTitle());
        }
        if (ticketRequestDto.getDescription() != null) {
            ticket.setDescription(ticketRequestDto.getDescription());
        }
        if (ticketRequestDto.getDueDate() != null) {
            ticket.setDueDate(LocalDateTime.of(ticketRequestDto.getDueDate(), LocalTime.of(21, 0, 0)));
        }
        if (ticketRequestDto.getStatus() != null) {
            ticket.setStatus(ticketRequestDto.getStatus());
        }
        if (ticketRequestDto.equals(TicketStatus.DONE) || ticketRequestDto.equals(TicketStatus.ACCEPTED) || ticketRequestDto.equals(TicketStatus.DELETED)) {
            if (!ticket.getOverdue().equals(TicketStatus.OVERDUE))
                ticket.setOverdue(null);
        }
    }

    @Scheduled(initialDelay = 1, fixedDelay = 120, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void updateDueStatus() {
        List<Ticket> ticketList = facade.getTicketRepository()
                .findAllWithStatuses(TicketStatus.BACKLOG, TicketStatus.IN_PROGRESS, LocalDateTime.now().plusDays(1), LocalDateTime.now().minusDays(4));
        ticketList.stream().filter(t -> t.getOverdue() != null).forEach(t -> t.setOverdue(null));
        ticketList.stream().filter(t -> t.getDueDate().minusDays(4).toLocalDate().isBefore(LocalDate.now()))
                .forEach(t -> {
                    t.setOverdue(TicketStatus.THREE_DAYS_LEFT);
                    kafkaTemplate.send("ticketTopic", UUID.randomUUID(), facade.getTicketMapper().toDto(t));
                });
        ticketList.stream().filter(t -> t.getDueDate().minusDays(3).toLocalDate().isBefore(LocalDate.now()))
                .forEach(t -> {
                    t.setOverdue(TicketStatus.TWO_DAYS_LEFT);
                    kafkaTemplate.send("ticketTopic", UUID.randomUUID(), facade.getTicketMapper().toDto(t));
                });
        ticketList.stream().filter(t -> t.getDueDate().minusDays(2).toLocalDate().isBefore(LocalDate.now()))
                .forEach(t -> {
                    t.setOverdue(TicketStatus.TODAY_LEFT);
                    kafkaTemplate.send("ticketTopic", UUID.randomUUID(), facade.getTicketMapper().toDto(t));
                });
        ticketList.stream().filter(t -> t.getDueDate().minusDays(1).toLocalDate().isBefore(LocalDate.now()))
                .forEach(t -> {
                    t.setOverdue(TicketStatus.OVERDUE);
                    kafkaTemplate.send("ticketTopic", UUID.randomUUID(), facade.getTicketMapper().toDto(t));
                });
    }

    @Transactional
    public void acceptTask(String username, UUID ticketId) {
        User user = userService.findUserByUsername(username);
        Ticket ticket = facade.getTicketRepository().findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (ticket.getReporter().equals(user)) {
            ticket.setStatus(TicketStatus.ACCEPTED);
            kafkaTemplate.send("ticketTopic", UUID.randomUUID(), facade.getTicketMapper().toDto(ticket));
        } else {
            throw new NoPermissionToChangeException("User: " + username + "has no rights to perform this action.");

        }
    }

    @Transactional
    public void rejectTicket(UUID ticketId, String username) {
        User reporter = userService.findUserByUsername(username);
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (ticket.getStatus().equals(TicketStatus.DONE) && ticket.getReporter().equals(reporter)) {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
            kafkaTemplate.send("ticketTopic", UUID.randomUUID(), facade.getTicketMapper().toDto(ticket));
        } else {
            throw new NoPermissionToChangeException("Unable to reject the task with id: " + ticketId);
        }
    }

    @Transactional
    public void setTicketDone(UUID ticketId, String username) {
        User assignee = userService.findUserByUsername(username);
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (ticket.getStatus().equals(TicketStatus.IN_PROGRESS) && ticket.getAssignee().equals(assignee)) {
            ticket.setStatus(TicketStatus.DONE);
            kafkaTemplate.send("ticketTopic", UUID.randomUUID(), facade.getTicketMapper().toDto(ticket));
        } else {
            throw new NoPermissionToChangeException("Unable to set status 'Done' for this task with id: " + ticketId);
        }
    }

    @Transactional
    public void startWorkingOnTask(UUID ticketId, String username) {
        User assignee = userService.findUserByUsername(username);
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        if (ticket.getStatus().equals(TicketStatus.BACKLOG) && ticket.getAssignee().equals(assignee)) {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
            kafkaTemplate.send("ticketTopic", UUID.randomUUID(), facade.getTicketMapper().toDto(ticket));
        } else {
            throw new NoPermissionToChangeException("Unable to set status 'IN_PROGRESS' for this task with id: " + ticketId);
        }
    }

    public Page<TicketResponseDto> findAllByUsername(String username, TicketPage page) {
        pageValidator.validate(page);
        Pageable pageable = PageRequest.of(page.getPage() - 1, page.getSize(), Sort.by("updatedAt").descending());
        return facade.getTicketRepository().findAllByAssigneeUsername(pageable, username)
                .map(ticket -> facade.getTicketMapper().toResponseDtoFromEntity(ticket));
    }

    public Page<TicketResponseDto> findAllByUsernameAndDepartment(String username, String role, Long departmentId,
                                                                  TicketPage page) {
        pageValidator.validate(page);
        if (role.equals("ROLE_USER")) {
            throw new NoPermissionToView("You have insufficient privileges to perform this action");
        } else {
            Pageable pageable = PageRequest.of(page.getPage() - 1, page.getSize(), Sort.by("updatedAt").descending());
            return facade.getTicketRepository().findAllByAssigneeUsernameAndDepartment(pageable, username, departmentId)
                    .map(ticket -> facade.getTicketMapper().toResponseDtoFromEntity(ticket));
        }
    }

    public Page<TicketResponseDto> findAllByAssigneeUsernameAndStatus(String username, String role, String status,
                                                                      TicketPage page) {
        if (page.getPage() < 1) {
            page.setPage(1);
        }
        if (page.getSize() < 1) {
            page.setSize(10);
        }
        Pageable pageable = PageRequest.of(page.getPage() - 1, page.getSize(), Sort.by("updatedAt").descending());
        return ticketRepository.findAllByAssigneeUsernameAndStatus(pageable, username, status)
                .map(ticket -> facade.getTicketMapper().toResponseDtoFromEntity(ticket));
    }

    public Page<TicketResponseDto> findAllByDepartmentAndStatus(Long departmentId, String status, TicketPage page) {
        if (page.getPage() < 1) {
            page.setPage(1);
        }
        if (page.getSize() < 1) {
            page.setSize(10);
        }
        Pageable pageable = PageRequest.of(page.getPage() - 1, page.getSize(), Sort.by("updatedAt").descending());
        return ticketRepository.findAllByDepartmentAndStatus(pageable, departmentId, status)
                .map(ticket -> facade.getTicketMapper().toResponseDtoFromEntity(ticket));
    }

    public Page<TicketResponseDto> findTicketByUsernameAndTitle(String username, String role, TicketPage page, String title) {
        if(role.equals("ROLE_USER")) {
            throw new NoPermissionToView("You have insufficient privileges to perform this action");
        }
        else {
        pageValidator.validate(page);
        User user = facade.getUserRepository().findByUsername(username);
        UUID userId = user.getId();
        Pageable pageable = PageRequest.of(page.getPage() - 1, page.getSize());
        return facade.getTicketRepository().findTicketsByUserAndTitle(pageable, userId, title)
                .map(ticket -> facade.getTicketMapper().toResponseDtoFromEntity(ticket));
        }
    }
}
