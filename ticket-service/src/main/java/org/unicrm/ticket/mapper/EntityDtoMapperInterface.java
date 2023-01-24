package org.unicrm.ticket.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.unicrm.ticket.dto.TicketDto;
import org.unicrm.ticket.entity.Ticket;

@Mapper
public interface EntityDtoMapperInterface {
    EntityDtoMapperInterface INSTANCE = Mappers.getMapper(EntityDtoMapperInterface.class);
    Ticket toEntity(TicketDto ticketDto);
    TicketDto toDto(Ticket ticket);
    
}
