package org.unicrm.ticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.ticket.dto.TicketDto;
import org.unicrm.ticket.entity.Ticket;
import org.unicrm.ticket.exception.ResourceNotFoundException;
import org.unicrm.ticket.mapper.EntityDtoMapperInterface;
import org.unicrm.ticket.repository.TicketRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    public List<TicketDto> findAll() {
        return ticketRepository.findAll().stream().map(EntityDtoMapperInterface.INSTANCE::toDto).collect(Collectors.toList());
    }

    public TicketDto findTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        return EntityDtoMapperInterface.INSTANCE.toDto(ticket);
    }

    public void deleteById(Long id) {
        ticketRepository.deleteById(id);
    }

    public Ticket createTicket(TicketDto ticketDto) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Ticket ticket = EntityDtoMapperInterface.INSTANCE.toEntity(ticketDto);
        ticket.setCreatedAt(timestamp);
        return ticketRepository.save(ticket);
    }


    @Transactional
    public TicketDto update(TicketDto ticketDto) {
        Ticket ticket = ticketRepository.findById(ticketDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to update ticket. Ticket not found, id: " + ticketDto.getId()));
        ticket.setTitle(ticketDto.getTitle());
        ticket.setStatus(ticketDto.getStatus());
        ticket.setDescription(ticketDto.getDescription());
        ticket.setAssigneeId(ticketDto.getAssigneeId());
        ticket.setReporterId(ticketDto.getReporterId());
        ticket.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        ticket.setDueDate(ticketDto.getDueDate());
        return ticketDto;
    }
}
