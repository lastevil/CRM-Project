package org.unicrm.ticket.mapper;

import org.mapstruct.factory.Mappers;
import org.unicrm.ticket.dto.TicketDepartmentDto;
import org.unicrm.ticket.entity.TicketDepartment;

public interface TicketDepartmentMapper {
    TicketDepartmentMapper INSTANCE = Mappers.getMapper(TicketDepartmentMapper.class);

    TicketDepartment toEntity(TicketDepartmentDto ticketDepartment);

    TicketDepartmentDto toDto(TicketDepartment ticketDepartment);


}
