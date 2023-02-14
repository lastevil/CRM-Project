package org.unicrm.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.util.*;
@Data
@Builder
public class ChatGroupDto {
    private String chatdate;
    private String message;
    private String status;
    private Long groupId;
    private UUID senderId;
    private UUID recipientId;
}
