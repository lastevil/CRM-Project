package org.unicrm.analytic.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.User;
import org.unicrm.lib.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(target = "id",source = "userDto.id")
    @Mapping(target = "department",source = "department")
    public User fromUserDto(UserDto userDto, Department department);
}
