package org.unicrm.chat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.chat.entity.Group;
import org.unicrm.lib.dto.UserDto;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);
    @Mapping(source = "departmentId", target = "id")
    @Mapping(source = "departmentTitle", target = "title")
    Group fromGlobalDto(UserDto userDto);
}
