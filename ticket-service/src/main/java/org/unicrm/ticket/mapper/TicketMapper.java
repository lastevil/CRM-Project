package org.unicrm.ticket.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.factory.Mappers;
import org.unicrm.ticket.dto.TicketDto;
import org.unicrm.ticket.entity.Ticket;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    Ticket toEntity(TicketDto ticketDto);

    TicketDto toDto(Ticket ticket);

}
