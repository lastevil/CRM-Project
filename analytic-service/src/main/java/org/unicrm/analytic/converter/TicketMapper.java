package org.unicrm.analytic.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.analytic.dto.TicketResponseDto;
import org.unicrm.analytic.dto.kafka.KafkaTicketDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    @Mapping(target = "id", source = "ticketDto.id")
    @Mapping(target = "title",source = "ticketDto.title")
    @Mapping(target = "reporter", source = "reporter")
    @Mapping(target = "assignee", source = "assignee")
    @Mapping(target = "department", source = "ticketDepartment")
    Ticket fromTicketDto(KafkaTicketDto ticketDto, User reporter, User assignee, Department ticketDepartment);

    @Mapping(target = "assignee.id",source = "assignee.id")
    @Mapping(target = "assignee.firstName",source = "assignee.firstName")
    @Mapping(target = "assignee.lastName",source = "assignee.lastName")
    @Mapping(target = "reporter.id",source = "reporter.id")
    @Mapping(target = "reporter.firstName",source = "reporter.firstName")
    @Mapping(target = "reporter.lastName",source = "reporter.lastName")
    @Mapping(target = "department.id",source = "department.id")
    @Mapping(target = "department.title",source = "department.title")
    TicketResponseDto fromEntityToFrontDto(Ticket ticket);

    default OffsetDateTime map(String value) {
        LocalDateTime localDateTime = LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toOffsetDateTime();
    }
}
