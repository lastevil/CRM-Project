package org.unicrm.ticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.unicrm.ticket.dto.*;
import org.unicrm.ticket.services.TicketService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
@Tag(name = "Заявки", description = "Контроллер для обработки запросов сервиса заявок")
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "метод для получения всех заявок")
    @GetMapping()
    public Page<TicketResponseDto> getAllTickets(@Valid TicketPage page) {
        return ticketService.findAll(page);
    }

    @Operation(summary = "метод для получения конкретной заявки по ее id")
    @GetMapping("/{id}")
    public TicketResponseDto getTicketById(@PathVariable UUID id) {
        return ticketService.findTicketById(id);
    }

    @Operation(summary = "метод создания заявки")
    @PostMapping("/create/{departmentId}/{assigneeId}")
    public void createTicket(@RequestBody TicketRequestDto ticketDto, @PathVariable Long departmentId,
                             @PathVariable(required = false) UUID assigneeId, @RequestHeader String username) {
        ticketService.createTicket(ticketDto, departmentId, assigneeId, username);
    }

    @Operation(summary = "метод для обновления заявки")
    @PutMapping("/update/{id}/{departmentId}/{assigneeId}")
    public void updateTicket(@RequestBody TicketRequestDto ticketDto, @PathVariable UUID id, @PathVariable(required = false) Long departmentId,
                             @PathVariable(required = false) UUID assigneeId) {
        ticketService.update(ticketDto, id, departmentId, assigneeId);
    }

    @Operation(summary = "метод получения заявок по пользователю и отделу")
    @GetMapping()
    public Page<TicketResponseDto> getTicketsByUsername(@RequestHeader String username, @Valid TicketPage page) {
        return ticketService.findAllByUsername(username, page);
    }

    @Operation(summary = "метод удаления конкретной заявки по ее id")
    @DeleteMapping("/{id}")
    public void deleteTicketById(@PathVariable UUID id, @RequestHeader String username) {
        ticketService.deleteById(id, username);
    }

    @Operation(summary = "метод получения списка всех заявок по id исполнителю")
    @GetMapping("/assignee/{assigneeId}")
    public Page<TicketResponseDto> getAllByAssignee(@Valid TicketPage page, @PathVariable UUID assigneeId) {
        return ticketService.findTicketsByAssignee(assigneeId, page);
    }

    @Operation(summary = "метод для получения списка заявок по id исполнителю и статусу")
    @GetMapping("/{assigneeId}/{status}")
    public Page<TicketResponseDto> getAllByAssigneeAndStatus(@PathVariable UUID assigneeId, @PathVariable String status, @Valid TicketPage page) {
        return ticketService.findTicketsByAssigneeAndStatus(assigneeId, status, page);
    }

    @Operation(summary = "метод для получения списка заявок по username исполнителя и статусу")
    @GetMapping("/{status}")
    public Page<TicketResponseDto> getAllByAssigneeUsernameAndStatus(@RequestHeader String username,
                                                                     @PathVariable String status,
                                                                     @Valid TicketPage page) {
        return ticketService.findAllByAssigneeUsernameAndStatus(username, status, page);
    }

    @Operation(summary = "метод для получения списка заявок по отделу и статусу")
    @GetMapping("/{depatments}/{status}")
    public Page<TicketResponseDto> getAllByDepartmentAndStatus(@PathVariable Long departmentId,
                                                               @PathVariable String status,
                                                               @Valid TicketPage page) {
        return ticketService.findAllByDepartmentAndStatus(departmentId, status, page);
    }

    @Operation(summary = "метод для получения списка заявок по частичному совпадение заголовка")
    @GetMapping("/search/{title}")
    public Page<TicketResponseDto> getTicketsByTitle(@PathVariable String title, @Valid TicketPage page) {
        return ticketService.findTicketByTitle(page, title);
    }

    @Operation(summary = "метод для получения списка заявок по отделу")
    @GetMapping("/department/{departmentId}")
    public Page<TicketResponseDto> getTicketsByDepartment(@PathVariable Long departmentId, @Valid TicketPage page) {
        return ticketService.findTicketsByDepartment(page, departmentId);
    }

    @Operation(summary = "метод получения заявки по статусу")
    @GetMapping("/status/{status}")
    public Page<TicketResponseDto> getTicketsByStatus(@PathVariable String status, @Valid TicketPage page) {
        return ticketService.findTicketByStatus(page, status);
    }

    @Operation(summary = "метод для передачи заявки в работу и присвоения статуса IN_PROGRESS")
    @PostMapping("/ticket/progress/{ticketId}")
    public void takeTicketToWork(@PathVariable UUID ticketId, @RequestHeader String username) {
        ticketService.startWorkingOnTask(ticketId, username);
    }

    @Operation(summary = "метод для принятия заявки и передачи в статус ACCEPTED")
    @PostMapping("/ticket/accept/{ticketId}")
    public void acceptTicket(@RequestHeader String username, @PathVariable UUID ticketId) {
        ticketService.acceptTask(username, ticketId);
    }

    @Operation(summary = "метод для выборки заявок по ответственному")
    @GetMapping("/ticket/reporter/{reporter}")
    public Page<TicketResponseDto> getTicketsByReporter(@Valid TicketPage page, @PathVariable String reporter) {
        return ticketService.findAllByReporter(page, reporter);
    }

    @Operation(summary = "метод для отклонения выполненной заявки")
    @PostMapping("/ticket/reject/{ticketId}/{reporter}")
    public void rejectTicket(@PathVariable UUID ticketId, @PathVariable String reporter) {
        ticketService.rejectTicket(ticketId, reporter);
    }

    @Operation(summary = "метод для перевода заявки в статус DONE")
    @PostMapping("/ticket/done/{ticketId}")
    public void setTicketDone(@PathVariable UUID ticketId, @RequestHeader String username) {
        ticketService.setTicketDone(ticketId, username);
    }
}
