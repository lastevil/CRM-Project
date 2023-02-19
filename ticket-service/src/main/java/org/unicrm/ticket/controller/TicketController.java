package org.unicrm.ticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unicrm.ticket.dto.TicketDto;
import org.unicrm.ticket.dto.TicketRequestDto;
import org.unicrm.ticket.dto.TicketResponseDto;
import org.unicrm.ticket.services.TicketService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Заявки", description = "Контроллер для обработки запросов сервиса заявок")
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "метод для получения всех заявок")
    @GetMapping()
    public List<TicketDto> getAllTickets() {
        return ticketService.findAll();
    }

    @Operation(summary = "метод для получения конкретной заявки по ее id")
    @GetMapping("/{id}")
    public TicketDto getTicketById(@PathVariable UUID id) {
        return ticketService.findTicketById(id);
    }

    @Operation(summary = "метод создания заявки")
    @PostMapping("/create/{departmentId}/{assigneeId}")
    public void createTicket(@RequestBody TicketRequestDto ticketDto, @PathVariable Long departmentId,
                             @PathVariable(required = false) UUID assigneeId, @RequestHeader String username) {
        ticketService.createTicket(ticketDto, departmentId, assigneeId, username);
    }

    @Operation(summary = "метод для обновления заявки")
    @PutMapping("update/{id}/{departmentId}/{assigneeId}")
    public void updateTicket(@RequestBody TicketRequestDto ticketDto, @PathVariable UUID id, @PathVariable(required = false) Long departmentId,
                             @PathVariable(required = false) UUID assigneeId) {
        ticketService.update(ticketDto,id,departmentId,assigneeId);
    }

    @Operation(summary = "метод удаления конкретной заявки по ее id")
    @DeleteMapping("/{id}")
    public void deleteTicketById(@PathVariable UUID id) {
        ticketService.deleteById(id);
    }

    @Operation(summary = "метод получения списка всех заявок по исполнителю")
    @GetMapping("/tickets/assignee/{assigneeId}")
    public List<TicketResponseDto> getAllByAssignee(@PathVariable UUID assigneeId) {
        return ticketService.findTicketsByAssignee(assigneeId);
    }

    @Operation(summary = "метод для получения списка заявок по исполнителю и статусу")
    @GetMapping("/tickets/{assigneeId}/{status}")
    public List<TicketResponseDto> getAllByAssigneeAndStatus(@PathVariable UUID assigneeId, @PathVariable String status) {
        return ticketService.findTicketsByAssigneeAndStatus(assigneeId, status);
    }

    @Operation(summary = "метод для получения списка заявок по частичному совпадение заголовка")
    @GetMapping("/tickets/search/{title}")
    public List<TicketResponseDto> getTicketsByTitle(@PathVariable String title) {
        return ticketService.findTicketByTitle(title);
    }

    @Operation(summary = "метод для получения списка заявок по отделу")
    @GetMapping("/tickets/department/{id}")
    public List<TicketResponseDto> getTicketsByDepartment(@PathVariable Long departmentId) {
        return ticketService.findTicketsByDepartment(departmentId);
    }


}
