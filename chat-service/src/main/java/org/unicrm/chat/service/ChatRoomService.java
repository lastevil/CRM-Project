package org.unicrm.chat.service;

import org.unicrm.chat.model.ChatHistory;
import org.unicrm.chat.model.ChatMessage;
import org.unicrm.chat.entity.ChatRoom;
import org.unicrm.chat.repository.ChatRoomRepository;
import org.unicrm.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private String date = null;

    @Transactional
    public Long save(ChatMessage chatMessage) {
        date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
        chatRoomRepository.insert(chatMessage.getSenderId(), chatMessage.getRecipientId(),
                date, "MessageStatus.RECEIVED", chatMessage.getContent());
        return chatRoomRepository.findMaxId();
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

    public List<ChatRoom> findBySenderIdAndRecipientId(ChatHistory chatHistory){
        List<ChatRoom> chatRooms = chatRoomRepository.findBySenderIdAndRecipientId(chatHistory.getSenderId(),
                chatHistory.getRecipientId());

        return chatRooms;
    }
    public Optional<ChatRoom> findById(Long id){
        return chatRoomRepository.findById(id);
    }
    public int findBySenderIdAndStatus(Long senderId, Long recipientId){
        List<ChatRoom> chatRooms = chatRoomRepository.findBySenderIdAndRecipientIdAndStatus(

                recipientId,senderId,
                "MessageStatus.RECEIVED");
        return chatRooms.size();
    }

}
