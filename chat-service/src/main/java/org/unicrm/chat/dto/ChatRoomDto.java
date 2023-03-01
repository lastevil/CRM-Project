package org.unicrm.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDto {
    private String chatdate;
    private String message;
    private String status;
    private UUID recipientId;
    private UUID senderId;
}
