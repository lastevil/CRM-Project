package org.unicrm.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChatGroups {
    private Long id;
    private String title;
    private int count; //количество уведомлений

    public ChatGroups() {
    }
}