package org.unicrm.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unicrm.chat.entity.ChatRoom;

import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistory {
    private Long id;
    private MessageType type;
    private String content;
    private String sender;
    private UUID senderId;
    private UUID recipientId;
    private String recipientName;
    private Long groupId;
    private List<ChatRoom> chatRoom;
    private List<ChatGroupHistory> chatGroup;
    private String chatDate;

}