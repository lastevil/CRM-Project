package org.unicrm.ticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unicrm.ticket.dto.TicketDto;
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
    @PostMapping()
    public TicketDto createTicket(@RequestBody TicketDto ticketDto) {
        ticketService.createTicket(ticketDto);
        return ticketDto;
    }

    @Operation(summary = "метод для обновления заявки")
    @PutMapping
    public void updateTicket(@RequestBody TicketDto ticketDto) {
        ticketService.update(ticketDto);
    }

    @Operation(summary = "метод удаления конкретной заявки по ее id")
    @DeleteMapping("/{id}")
    public void deleteTicketById(@PathVariable UUID id) {
        ticketService.deleteById(id);
    }

//    @Operation(summary = "метод получения списка всех заявок по исполнителю")
//    @PostMapping("/filter/by-assignee")
//    public List<TicketDto> getAllByAssignee(@RequestBody UserDto assignee) {
//        return ticketService.findTicketsByAssignee(assignee);
//    }


}
