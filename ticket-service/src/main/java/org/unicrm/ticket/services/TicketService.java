package org.unicrm.ticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.ticket.dto.TicketDto;
import org.unicrm.ticket.dto.TicketUserDto;
import org.unicrm.ticket.entity.Ticket;
import org.unicrm.ticket.entity.TicketDepartment;
import org.unicrm.ticket.entity.TicketUser;
import org.unicrm.ticket.exception.ResourceNotFoundException;
import org.unicrm.ticket.mapper.TicketMapperInterface;
import org.unicrm.ticket.mapper.TicketUserMapper;
import org.unicrm.ticket.repository.TicketRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapperInterface ticketMapper = TicketMapperInterface.INSTANCE;
    private final TicketUserMapper userMapper = TicketUserMapper.INSTANCE;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    public List<TicketDto> findAll() {
        return ticketRepository.findAll().stream().map(ticketMapper::toDto).collect(Collectors.toList());
    }

    public TicketDto findTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        return ticketMapper.toDto(ticket);
    }

    public void deleteById(UUID id) {
        ticketRepository.deleteById(id);
    }

    public Ticket createTicket(TicketDto ticketDto) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Ticket ticket = ticketMapper.toEntity(ticketDto);
        ticket.setCreatedAt(timestamp);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket update(TicketDto ticketDto) {
        Ticket ticket = ticketRepository.findById(ticketDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Failed to update ticket. Ticket not found, id: " + ticketDto.getId()));
        ticket.setTitle(ticketDto.getTitle());
        ticket.setStatus(ticketDto.getStatus());
        ticket.setDescription(ticketDto.getDescription());
        ticket.setAssigneeId(ticketDto.getAssigneeId());
        ticket.setReporterId(ticketDto.getReporterId());
        ticket.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        ticket.setDueDate(ticketDto.getDueDate());
        return ticket;
    }

    public List<TicketDto> findTicketsByAssignee(TicketUserDto userDto) {
        return ticketRepository.findAllByAssignee(userMapper.toEntity(userDto))
                .stream().map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }

    //TODO: Fix
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

    public Integer countTicketsByDepartmentAndStatus(TicketDepartment ticketDepartment, String status) {
        return ticketRepository.countAllByDepartmentAndStatus(ticketDepartment.getDepartmentId(), status);
    }

    public Integer countTicketsByAssigneeAndStatus(TicketUser assignee, String status) {
        return ticketRepository.countAllByAssigneeIdAndStatus(assignee.getId(), status);
    }

    public Integer countTicketsByReporterAndStatus(TicketUser reporter, String status) {
        return ticketRepository.countAllByReporterIdAndStatus(reporter.getId(), status);
    }
}
