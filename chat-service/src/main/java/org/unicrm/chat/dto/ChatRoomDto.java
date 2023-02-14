package org.unicrm.chat.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;


@Data
@Builder
public class ChatRoomDto {
    private String chatdate;
    private String message;
    private String  status;
    private UUID recipientId;
    private UUID senderId;

}
