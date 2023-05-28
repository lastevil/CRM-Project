package org.unicrm.auth.dto.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.unicrm.auth.entities.Status;

import java.util.UUID;

@Data
@NoArgsConstructor
public class KafkaUserDto {

    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private Long departmentId;
    private String departmentTitle;
    private Status status;
}
