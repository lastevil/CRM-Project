package org.unicrm.chat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.chat.entity.User;
import org.unicrm.chat.model.UserRegistration;
import org.unicrm.lib.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "uuid")
    @Mapping(expression = "java(userDto.getLastName()+\" \"+userDto.getFirstName())", target = "nickName")
    @Mapping(source = "userDto.username", target = "userName")
    @Mapping(target = "groups", ignore = true)
    User toEntity(UserDto userDto);
}
