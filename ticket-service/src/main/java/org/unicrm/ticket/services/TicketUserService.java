package org.unicrm.ticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unicrm.ticket.dto.TicketDepartmentDto;
import org.unicrm.ticket.dto.TicketUserDto;
import org.unicrm.ticket.entity.TicketDepartment;
import org.unicrm.ticket.entity.TicketUser;
import org.unicrm.ticket.exception.ResourceNotFoundException;
import org.unicrm.ticket.mapper.TicketUserMapper;
import org.unicrm.ticket.repository.TicketUserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketUserService {
    private final TicketUserRepository ticketUserRepository;

    private final TicketUserMapper mapper;

    //TODO: change to findUserByUsername()
    public TicketUser findUserById(UUID uuid) {
        return ticketUserRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}
