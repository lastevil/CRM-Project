package org.unicrm.ticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.ticket.dto.TicketDto;
import org.unicrm.ticket.dto.TicketUserDto;
import org.unicrm.ticket.entity.Ticket;
import org.unicrm.ticket.entity.TicketDepartment;
import org.unicrm.ticket.exception.ResourceNotFoundException;
import org.unicrm.ticket.mapper.TicketMapperInterface;
import org.unicrm.ticket.mapper.TicketUserMapper;
import org.unicrm.ticket.repository.TicketRepository;
import org.unicrm.ticket.repository.TicketUserRepository;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketUserRepository userRepository;
    private final TicketMapperInterface ticketMapper = TicketMapperInterface.INSTANCE;
    private final TicketUserMapper userMapper = TicketUserMapper.INSTANCE;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private final TicketUserRepository ticketUserRepository;

    public List<TicketDto> findAll() {
        return ticketRepository.findAll().stream().map(ticketMapper::toDto).collect(Collectors.toList());
    }

    public TicketDto findTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        return ticketMapper.toDto(ticket);
    }

    @Transactional
    public void deleteById(UUID id) {
        ticketRepository.deleteById(id);
    }

    @Transactional
    public Ticket createTicket(TicketDto ticketDto) {
        LocalDateTime creationTime = LocalDateTime.now();
        Ticket ticket = ticketMapper.toEntity(ticketDto);
        ticket.setCreatedAt(creationTime);
        ticket.setIsOverdue(creationTime.isAfter(creationTime));
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket update(TicketDto ticketDto) {
        Date today = new Date(System.currentTimeMillis());
        Ticket ticket = ticketRepository.findById(ticketDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to update ticket. Ticket not found, id: " + ticketDto.getId()));
        ticket.setTitle(ticketDto.getTitle());
        ticket.setStatus(ticketDto.getStatus());
        ticket.setDescription(ticketDto.getDescription());
        ticket.setAssigneeId(ticketDto.getAssigneeId());
        ticket.setReporterId(ticketDto.getReporterId());
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setDueDate(ticketDto.getDueDate());
        ticket.setIsOverdue(today.after(ticketDto.getDueDate()));
        return ticket;
    }

    public List<TicketDto> findTicketsByAssignee(TicketUserDto userDto) {
        return ticketRepository.findAllByAssignee(userMapper.toEntity(userDto))
                .stream().map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TicketDto> findTicketsByDepartment(TicketDepartment ticketDepartment) {
        return ticketRepository.findAllByDepartment(ticketDepartment.getDepartmentId())
                .stream().map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TicketDto> findTicketsByAssigneeAndStatus(TicketUserDto assignee, String status) {
        return ticketRepository.findAllByAssigneeIdAndStatus(assignee.getId(), status)
                .stream().map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }
}
