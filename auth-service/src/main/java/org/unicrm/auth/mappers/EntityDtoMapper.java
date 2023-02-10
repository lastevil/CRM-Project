package org.unicrm.auth.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.auth.dto.DepartmentDto;
import org.unicrm.auth.dto.UserRegDto;
import org.unicrm.auth.entities.Department;
import org.unicrm.auth.entities.User;
import org.unicrm.lib.dto.UserDto;

@Mapper
public interface EntityDtoMapper {
    EntityDtoMapper INSTANCE = Mappers.getMapper(EntityDtoMapper.class);

    User toEntity(UserDto userDto);

    User toEntity(UserRegDto userRegDto);

    @Mapping(source = "uuid", target = "id")
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.title", target = "departmentTitle")
    UserDto toDto(User user);

    DepartmentDto toDto(Department department);
}