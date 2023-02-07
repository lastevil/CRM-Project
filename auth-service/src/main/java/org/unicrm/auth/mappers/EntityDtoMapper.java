package org.unicrm.auth.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.unicrm.auth.entities.User;
import org.unicrm.lib.dto.UserDto;

@Mapper
public interface EntityDtoMapper {
    EntityDtoMapper INSTANCE = Mappers.getMapper(EntityDtoMapper.class);
    User toEntity(UserDto userSimpleDto);
    UserDto toDto(User user);
}
