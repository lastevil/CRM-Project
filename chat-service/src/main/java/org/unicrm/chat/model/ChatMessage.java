package org.unicrm.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private MessageType type;
    private UUID senderId;
    private String senderName;
    private UUID recipientId;
    private String recipientName;
    private Long groupId;
    private String chatDate;
    private String message;

}