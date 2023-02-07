package org.unicrm.analytic.converter;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.analytic.dto.DepartmentFrontDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.lib.dto.UserDto;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    @Mapping(target = "id", source = "departmentId")
    @Mapping(target = "title", source = "departmentTitle")
    Department fromUserDto(UserDto userDto);

    DepartmentFrontDto fromEntityToFrontDto(Department d);
}
