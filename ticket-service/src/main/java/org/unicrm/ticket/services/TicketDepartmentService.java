package org.unicrm.ticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unicrm.lib.dto.UserDto;
import org.unicrm.ticket.entity.TicketDepartment;
import org.unicrm.ticket.mapper.TicketDepartmentMapper;
import org.unicrm.ticket.services.utils.TicketFacade;

@Service
@RequiredArgsConstructor
public class TicketDepartmentService {

    private final TicketDepartmentMapper mapper;

    public TicketDepartment findDepartmentById(UserDto userDto) {
        return mapper.toEntity(userDto);
    }
}
