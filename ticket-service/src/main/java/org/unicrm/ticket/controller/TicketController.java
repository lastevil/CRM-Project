package org.unicrm.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unicrm.ticket.dto.TicketDto;
import org.unicrm.ticket.services.TicketService;

import java.util.List;

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
    public TicketDto getTicketById(@PathVariable Long id) {
        return ticketService.findTicketById(id);
    }

    @PostMapping()
    public TicketDto createTicket(@RequestBody TicketDto ticketDto) {
        ticketService.createTicket(ticketDto);
        return ticketDto;
    }

    @PutMapping
    public TicketDto updateTicket(@RequestBody TicketDto ticketDto) {
        return ticketService.update(ticketDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTicketById(@PathVariable Long id) {
        ticketService.deleteById(id);
    }
}
