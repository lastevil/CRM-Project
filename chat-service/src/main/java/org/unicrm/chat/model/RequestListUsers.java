package org.unicrm.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class RequestListUsers {

    private UUID senderId;
    private List<ChatUsers> users;

    public RequestListUsers() {
    }
}
