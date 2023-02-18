package org.unicrm.ticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public TicketUser findUserById(UUID uuid) {
        return ticketUserRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    public TicketUser findUserByUsername(String username) {
        return ticketUserRepository.findByUsername(username);
    }

    public Long findDepartmentId(UUID id) {
        return ticketUserRepository.findUserDepartment(id);
    }
}
