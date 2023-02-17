package org.unicrm.ticket.mapper;

import org.mapstruct.Mapper;


import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.ticket.dto.TicketDepartmentDto;
import org.unicrm.ticket.dto.TicketDto;
import org.unicrm.ticket.dto.TicketUserDto;
import org.unicrm.ticket.entity.Ticket;
import org.unicrm.ticket.entity.TicketDepartment;
import org.unicrm.ticket.entity.TicketUser;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);


    @Mapping(source = "ticketDto.id", target = "id")
    @Mapping(source = "assignee", target = "assigneeId")
    @Mapping(source = "reporter", target = "reporterId")
    @Mapping(source = "department", target = "departmentId")
    @Mapping(source = "ticketDto.title", target = "title")
    Ticket toEntity(TicketDto ticketDto, TicketUserDto assignee, TicketUserDto reporter, TicketDepartmentDto department);


    TicketDto toDto(Ticket ticket);

}
