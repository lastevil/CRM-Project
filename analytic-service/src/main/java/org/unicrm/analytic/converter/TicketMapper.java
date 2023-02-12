package org.unicrm.analytic.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.analytic.dto.TicketFrontDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;
import org.unicrm.lib.dto.TicketDto;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    @Mapping(target = "id", source = "ticketDto.id")
    @Mapping(target = "title",source = "ticketDto.title")
    @Mapping(target = "reporter", source = "reporter")
    @Mapping(target = "assignee", source = "assignee")
    @Mapping(target = "department", source = "ticketDepartment")
    Ticket fromTicketDto(TicketDto ticketDto, User reporter, User assignee, Department ticketDepartment);

    @Mapping(target = "assignee.id",source = "assignee.id")
    @Mapping(target = "assignee.firstName",source = "assignee.firstName")
    @Mapping(target = "assignee.lastName",source = "assignee.lastName")
    @Mapping(target = "reporter.id",source = "reporter.id")
    @Mapping(target = "reporter.firstName",source = "reporter.firstName")
    @Mapping(target = "reporter.lastName",source = "reporter.lastName")
    @Mapping(target = "department.id",source = "department.id")
    @Mapping(target = "department.title",source = "department.title")
    @Mapping(target = "createdAt",source = "ticket.createdAt",dateFormat = "dd-MM-yyyy hh:mm:ss" )
    @Mapping(target = "updatedAt",source ="ticket.updatedAt", dateFormat = "dd-MM-yyyy hh:mm:ss")
    @Mapping(target = "dueDate",source = "ticket.dueDate", dateFormat = "dd-MM-yyyy hh:mm:ss")
    TicketFrontDto fromEntityToFrontDto(Ticket ticket);
}
