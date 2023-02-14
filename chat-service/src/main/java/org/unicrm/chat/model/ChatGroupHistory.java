package org.unicrm.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatGroupHistory {
    private String type;
    private UUID id;
    private String chatdate;
    private String message;
    private String status;
    private Long groupId;
    private UUID senderId;
    private String senderName;
    private UUID recipientId;
    private String recipientName;
}