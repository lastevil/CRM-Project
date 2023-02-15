package org.unicrm.ticket.services.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unicrm.ticket.mapper.TicketDepartmentMapper;
import org.unicrm.ticket.mapper.TicketMapper;
import org.unicrm.ticket.mapper.TicketUserMapper;
import org.unicrm.ticket.repository.TicketDepartmentRepository;
import org.unicrm.ticket.repository.TicketRepository;
import org.unicrm.ticket.repository.TicketUserRepository;

@Component
@RequiredArgsConstructor
@Getter
public class TicketFacade {
    private final TicketUserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketDepartmentRepository departmentRepository;

    private final TicketUserMapper userMapper;
    private final TicketMapper ticketMapper;
    private final TicketDepartmentMapper departmentMapper;
}
