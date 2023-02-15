package org.unicrm.chat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.unicrm.chat.dto.ChatRoomDto;
import org.unicrm.chat.entity.ChatRoom;

@Mapper
public interface ChatRoomMapper {
    ChatRoomMapper INSTANCE = Mappers.getMapper(ChatRoomMapper.class);
    ChatRoom toEntity(ChatRoomDto dto);
}
