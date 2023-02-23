package org.unicrm.analytic.dto.kafka;


import lombok.Data;
import lombok.NoArgsConstructor;

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

}
