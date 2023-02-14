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
public class ChatListUser {
    private UUID id;
    private String nicName;
    private int count; //количество уведомлений

}