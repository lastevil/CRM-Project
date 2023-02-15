package org.unicrm.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ChatUsers {
    private UUID id;
    private String nickName;
    private int count; //количество уведомлений

    public ChatUsers() {
    }

}