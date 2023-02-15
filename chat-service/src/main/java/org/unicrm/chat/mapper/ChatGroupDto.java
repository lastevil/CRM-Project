package org.unicrm.chat.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatGroupDto {

    private String chatdate;
    private String message;
    private String status;
    private Long groupId;
    private UUID senderId;
    private UUID recipientId;
}
