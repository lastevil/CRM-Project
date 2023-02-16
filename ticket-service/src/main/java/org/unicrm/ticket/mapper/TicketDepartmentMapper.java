package org.unicrm.ticket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.lib.dto.UserDto;
import org.unicrm.ticket.dto.TicketDepartmentDto;
import org.unicrm.ticket.entity.TicketDepartment;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface TicketDepartmentMapper {
    TicketDepartmentMapper INSTANCE = Mappers.getMapper(TicketDepartmentMapper.class);

    @Mapping(target = "departmentId", source = "departmentId")
    @Mapping(target = "title", source = "departmentTitle")
    TicketDepartment toEntity(UserDto ticketDepartment);

    TicketDepartmentDto toDto(Optional<TicketDepartment> ticketDepartment);


}
