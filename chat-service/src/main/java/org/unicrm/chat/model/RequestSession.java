package org.unicrm.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class RequestSession {

    private UUID senderId;
    private String session;

    public RequestSession() {
    }
}
