package org.unicrm.ticket.mapper;

import org.mapstruct.Mapper;


import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.lib.dto.TicketDto;
import org.unicrm.ticket.dto.*;
import org.unicrm.ticket.entity.Ticket;
import org.unicrm.ticket.entity.TicketDepartment;
import org.unicrm.ticket.entity.TicketUser;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "assigneeDepartmentId", source = "department.id")
    @Mapping(target = "reporterId", source = "reporter.id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "dueDate", source = "dueDate")
    @Mapping(target = "overdue", source = "overdue")
    TicketDto toDto(Ticket ticket);

    default OffsetDateTime map(String value) {
        LocalDateTime localDateTime = LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toOffsetDateTime();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "ticketDto.title")
    @Mapping(target = "description", source = "ticketDto.description")
    @Mapping(target = "assignee.firstName", source = "assignee.firstName")
    @Mapping(target = "assignee.lastName", source = "assignee.lastName")
    @Mapping(target = "assignee.id", source = "assignee.id")
    @Mapping(target = "reporter.id", source = "reporter.id")
    @Mapping(target = "reporter.firstName", source = "reporter.firstName")
    @Mapping(target = "reporter.lastName", source = "reporter.lastName")
    @Mapping(target = "department.id", source = "department.id")
    @Mapping(target = "department.title", source = "department.title")
    @Mapping(target = "dueDate", expression = "java(ticketDto.getDueDate().atTime(21, 00, 00))")
    Ticket toEntityFromTicketRequest(TicketRequestDto ticketDto, TicketUser assignee, TicketUser reporter, TicketDepartment department);

    TicketResponseDto toResponseDtoFromEntity(Ticket ticket);


}