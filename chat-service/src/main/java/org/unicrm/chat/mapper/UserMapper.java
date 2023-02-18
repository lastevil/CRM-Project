package org.unicrm.chat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.unicrm.chat.entity.User;
import org.unicrm.chat.model.UserRegistration;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User toEntity(UserRegistration userReg);
}
