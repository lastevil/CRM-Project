package org.unicrm.ticket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.ticket.dto.TicketDto;
import org.unicrm.ticket.entity.Ticket;

@Mapper(componentModel = "spring")
public interface TicketMapperInterface {
    TicketMapperInterface INSTANCE = Mappers.getMapper(TicketMapperInterface.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "assigneeId", target = "assigneeId")
    @Mapping(source = "reporterId", target = "reporterId")
    @Mapping(source = "departmentId", target = "departmentId")
    Ticket toEntity(TicketDto ticketDto);
    TicketDto toDto(Ticket ticket);

}
