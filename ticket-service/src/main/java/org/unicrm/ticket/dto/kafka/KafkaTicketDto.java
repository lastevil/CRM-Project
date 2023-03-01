package org.unicrm.ticket.dto.kafka;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class KafkaTicketDto {
    private UUID id;
    private String title;
    private String status;
    private UUID assigneeId;
    private Long assigneeDepartmentId;
    private UUID reporterId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;
    private String overdue;
}
