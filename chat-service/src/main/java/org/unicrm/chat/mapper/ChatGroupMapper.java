package org.unicrm.chat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.unicrm.chat.dto.ChatGroupDto;
import org.unicrm.chat.entity.ChatGroup;

@Mapper(componentModel = "spring")
public interface ChatGroupMapper {

    ChatGroupMapper INSTANCE = Mappers.getMapper(ChatGroupMapper.class);
    ChatGroup toEntity(ChatGroupDto dto);

}
