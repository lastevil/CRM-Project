package org.unicrm.chat.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.chat.entity.ChatRoom;
import org.unicrm.chat.mapper.ChatRoomDto;
import org.unicrm.chat.mapper.ChatRoomMapper;
import org.unicrm.chat.model.ChatMessage;
import org.unicrm.chat.model.UserHistory;
import org.unicrm.chat.repository.ChatRoomRepository;
import org.unicrm.chat.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private String date = null;

    @Transactional
    public UUID save(ChatMessage chatMessage) {
        date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
        ChatRoomDto dto = ChatRoomDto.builder()
                .chatdate(date)
                .message(chatMessage.getMessage())
                .status("RECEIVED")
                .recipientId(chatMessage.getRecipientId())
                .senderId(chatMessage.getSenderId())
                .build();
        ChatRoom chatRoom = ChatRoomMapper.INSTANCE.toEntity(dto);
        chatRoom.builder()
                .chatdate(dto.getChatdate())
                .message(dto.getMessage())
                .status(dto.getStatus())
                .recipientId(dto.getRecipientId())
                .senderId(dto.getSenderId())
                .build();
        chatRoomRepository.save(chatRoom);
        Optional<ChatRoom> chatRoom1 =
                chatRoomRepository.findBySenderIdAndRecipientIdAndChatdate(chatMessage.getSenderId(),
                        chatMessage.getRecipientId(),date);
        if (chatRoom1.isEmpty()){
            throw new ResourceNotFoundException(String.format("Сообщение в таблице ChatRoom не найдено."));
        }
        return chatRoom1.get().getUuid();
    }
    @Transactional
    public ChatRoom findBySenderIdAndRecipientIdAndChatdate(ChatMessage chatMessage){

        Optional<ChatRoom> chatRoom =
                chatRoomRepository.findBySenderIdAndRecipientIdAndChatdate(chatMessage.getSenderId(),
                        chatMessage.getRecipientId(), date);
        date = null;
        if (chatRoom.isEmpty()) return null;
        return chatRoom.get();
    }

    @Transactional
    public List<ChatRoom> findBySenderIdAndRecipientId(UserHistory userHistory){
        List<ChatRoom> chatRooms = chatRoomRepository.findBySenderIdAndRecipientId(userHistory.getSenderId(),
                userHistory.getRecipientId());
        for (ChatRoom c : chatRooms) {
            chatRoomRepository.update(userHistory.getRecipientId(), userHistory.getSenderId(),
                    "DELIVERED");
        }
        return chatRooms;
    }
    public Optional<ChatRoom> findById(UUID id){
        return chatRoomRepository.findById(id);
    }

    public int findBySenderIdAndRecipientIdAndStatus(UUID senderId, UUID recipientId){
        List<ChatRoom> chatRooms = chatRoomRepository.findBySenderIdAndRecipientIdAndStatus(

                recipientId,senderId,
                "RECEIVED");
        return chatRooms.size();
    }

}
