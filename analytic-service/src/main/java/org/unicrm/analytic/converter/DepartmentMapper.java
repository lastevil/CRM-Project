package org.unicrm.analytic.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.analytic.dto.DepartmentFrontDto;
import org.unicrm.analytic.dto.kafka.KafkaUserDto;
import org.unicrm.analytic.entities.Department;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    @Mapping(target = "id", source = "departmentId")
    @Mapping(target = "title", source = "departmentTitle")
    Department fromUserDto(KafkaUserDto userDto);

    DepartmentFrontDto fromEntityToFrontDto(Department d);
}
