package org.unicrm.ticket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.ticket.dto.DepartmentDto;
import org.unicrm.ticket.dto.kafka.KafkaUserDto;
import org.unicrm.ticket.entity.Department;

@Mapper(componentModel = "spring")
public interface TicketDepartmentMapper {
    TicketDepartmentMapper INSTANCE = Mappers.getMapper(TicketDepartmentMapper.class);

    @Mapping(target = "id", source = "departmentId")
    @Mapping(target = "title", source = "departmentTitle")
    Department toEntity(KafkaUserDto userDto);

    DepartmentDto toDto(Department department);


}
