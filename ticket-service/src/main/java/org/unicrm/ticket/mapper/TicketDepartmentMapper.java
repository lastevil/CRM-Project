package org.unicrm.ticket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.ticket.dto.TicketDepartmentDto;
import org.unicrm.ticket.dto.kafka.KafkaUserDto;
import org.unicrm.ticket.entity.TicketDepartment;

@Mapper(componentModel = "spring")
public interface TicketDepartmentMapper {
    TicketDepartmentMapper INSTANCE = Mappers.getMapper(TicketDepartmentMapper.class);

    @Mapping(target = "id", source = "departmentId")
    @Mapping(target = "title", source = "departmentTitle")
    TicketDepartment toEntity(KafkaUserDto userDto);

    TicketDepartmentDto toDto(TicketDepartment ticketDepartment);


}
