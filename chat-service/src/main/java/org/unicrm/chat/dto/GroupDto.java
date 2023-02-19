package org.unicrm.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupDto {
    private Long id;
    private String title;
}
