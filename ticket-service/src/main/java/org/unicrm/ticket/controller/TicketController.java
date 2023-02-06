package org.unicrm.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unicrm.ticket.dto.TicketDto;
import org.unicrm.ticket.dto.TicketUserDto;
import org.unicrm.ticket.entity.Ticket;
import org.unicrm.ticket.services.TicketService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping()
    public List<TicketDto> getAllTickets() {
        return ticketService.findAll();
    }

    @GetMapping("/{id}")
    public TicketDto getTicketById(@PathVariable UUID id) {
        return ticketService.findTicketById(id);
    }

    @PostMapping()
    public TicketDto createTicket(@RequestBody TicketDto ticketDto) {
        ticketService.createTicket(ticketDto);
        return ticketDto;
    }

    @PutMapping
    public Ticket updateTicket(@RequestBody TicketDto ticketDto) {
        return ticketService.update(ticketDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTicketById(@PathVariable UUID id) {
        ticketService.deleteById(id);
    }

    //Методы фильтрации

    @PostMapping("/filter/by-assignee")
    public List<TicketDto> getAllByAssignee(@RequestBody TicketUserDto assignee) {
        return ticketService.findTicketsByAssignee(assignee);
    }

}
