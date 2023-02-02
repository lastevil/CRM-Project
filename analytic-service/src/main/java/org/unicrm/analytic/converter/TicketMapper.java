package org.unicrm.analytic.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;
import org.unicrm.lib.dto.TicketDto;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);
    @Mapping(target = "id", source = "ticketDto.id")
    @Mapping(target = "reporter", source = "reporter")
    @Mapping(target = "assignee", source = "assignee")
    @Mapping(target = "department", source = "assigneeDepartment")
    Ticket fromTicketDto(TicketDto ticketDto, User reporter, User assignee, Department assigneeDepartment);
}
