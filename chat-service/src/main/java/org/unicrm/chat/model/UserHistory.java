package org.unicrm.chat.model;

import lombok.Builder;
import lombok.Data;

import java.util.*;
@Data
@Builder
public class UserHistory {
    private UUID senderId;
    private UUID recipientId;
    private Long groupId;
}
