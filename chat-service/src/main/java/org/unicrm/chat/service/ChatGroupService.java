package org.unicrm.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.chat.dto.ChatGroupDto;
import org.unicrm.chat.entity.ChatGroup;
import org.unicrm.chat.mapper.ChatGroupMapper;
import org.unicrm.chat.model.ChatHistory;
import org.unicrm.chat.model.ChatMessage;
import org.unicrm.chat.repository.ChatGroupRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatGroupService {
    private final ChatGroupRepository chatGroupRepository;

    @Transactional
    public void save(ChatMessage chatMessage, UUID recipientId, String date) {
        String status;

        if(chatMessage.getSenderId().equals(recipientId)) {
            status = "DELIVERED";
        }else {
            status = "RECEIVED";
        }
        ChatGroupDto dto = ChatGroupDto.builder()
                .chatdate(date)
                .message(chatMessage.getMessage())
                .status(status)
                .groupId(chatMessage.getGroupId())
                .senderId(chatMessage.getSenderId())
                .recipientId(recipientId)
                .build();
        ChatGroup group = ChatGroupMapper.INSTANCE.toEntity(dto);
        group.builder()
                .chatdate(dto.getChatdate())
                .message(dto.getMessage())
                .status(dto.getStatus())
                .groupId(dto.getGroupId())
                .senderId(dto.getSenderId())
                .recipientId(dto.getRecipientId())
                .build();
        chatGroupRepository.save(group);
    }

    public Optional<ChatGroup> findById(UUID id) {
        return chatGroupRepository.findById(id);
    }

    public Optional<ChatGroup> findByChatdateAndRecipientId(String date, UUID recipientId) {
        return chatGroupRepository.findByChatdateAndRecipientId(date, recipientId);
    }

    @Transactional
    public List<ChatGroup> findByGroupIdAndRecipientId(ChatHistory userHistory) {
        List<ChatGroup> chatGroups = chatGroupRepository.findByGroupIdAndRecipientId(
                userHistory.getGroupId(), userHistory.getSenderId());
        for (ChatGroup c : chatGroups) {
            chatGroupRepository.update(userHistory.getGroupId(), userHistory.getSenderId(),
                    "DELIVERED");
        }
        return chatGroups;
    }

    public int findByGroupIdAndRecipientIdAndStatus(Long groupId, UUID recipientId) {
        List<ChatGroup> chatGroups = chatGroupRepository.findByGroupIdAndRecipientIdAndStatus(
                groupId, recipientId, "RECEIVED");
        return chatGroups.size();
    }
}
