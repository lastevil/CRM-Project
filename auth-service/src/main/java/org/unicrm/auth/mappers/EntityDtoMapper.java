package org.unicrm.auth.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.unicrm.auth.dto.UserDto;
import org.unicrm.auth.entities.User;

@Mapper
public interface EntityDtoMapper {
    EntityDtoMapper INSTANCE = Mappers.getMapper(EntityDtoMapper.class);
    User toEntity(UserDto userDto);
    UserDto toDto(User user);
}
