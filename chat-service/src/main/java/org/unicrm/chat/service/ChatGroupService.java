package org.unicrm.chat.service;

import org.unicrm.chat.entity.ChatGroup;
import org.unicrm.chat.entity.ChatGroup;
import org.unicrm.chat.entity.ChatRoom;
import org.unicrm.chat.model.ChatHistory;
import org.unicrm.chat.model.ChatMessage;
import org.unicrm.chat.repository.ChatGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatGroupService {
    private final ChatGroupRepository chatGroupRepository;


    public List<ChatGroup> findAll() {
        return chatGroupRepository.findAll();
    }

    @Transactional
    public void save(ChatMessage chatMessage, Long recipientId, String date) {

        chatGroupRepository.insert(date, chatMessage.getContent(), "MessageStatus.RECEIVED",
                chatMessage.getGroupId(), chatMessage.getSenderId(), recipientId
        );
        //  return chatGroupRepository.findMaxId();
    }

    public Optional<ChatGroup> findById(Long id) {
        return chatGroupRepository.findById(id);
    }

    public Optional<ChatGroup> findByChatdateAndRecipientId(String date, Long recipientId) {
        return chatGroupRepository.findByChatdateAndRecipientId(date, recipientId);
    }

    @Transactional
    public List<ChatGroup> findByGroupIdAndRecipientId(ChatHistory chatHistory) {
        List<ChatGroup> chatGroups = chatGroupRepository.findByGroupIdAndRecipientId(
                chatHistory.getGroupId(), chatHistory.getSenderId());
        return chatGroups;
    }

}
