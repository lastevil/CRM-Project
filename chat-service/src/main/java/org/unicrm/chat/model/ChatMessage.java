package org.unicrm.chat.model;

import lombok.NoArgsConstructor;
import org.unicrm.chat.entity.ChatGroup;
import org.unicrm.chat.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private UUID senderId;
    private String senderName;
    private UUID recipientId;
    private String recipientName;
    private Long groupId;
    private List<ChatListGroup> chatGroup;
    private List<ChatListUser> chatUsers;
    private Long chatroomId;
    private Long chatgroupId;
    private List<ChatRoom> chatRoom;
    private List<ChatGroup> chatGroups;
    private String chatDate;
    private List<UUID> allIdUsers;
    private String message;
}