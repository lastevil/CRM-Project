package org.unicrm.chat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.unicrm.chat.dto.GroupDto;
import org.unicrm.chat.entity.Group;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    Group toEntity(GroupDto dto);

    GroupDto toDto(Group group);
}
