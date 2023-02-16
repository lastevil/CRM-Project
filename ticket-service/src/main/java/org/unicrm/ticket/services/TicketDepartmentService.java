package org.unicrm.ticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unicrm.lib.dto.UserDto;
import org.unicrm.ticket.entity.TicketDepartment;
import org.unicrm.ticket.services.utils.TicketFacade;

@Service
@RequiredArgsConstructor
public class TicketDepartmentService {

    private final TicketFacade facade;

    public TicketDepartment findDepartmentById(UserDto userDto) {
        return facade.getDepartmentMapper().toEntity(userDto);
    }
}
