package org.unicrm.lib.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TicketDto {
    private UUID id;
    private String title;
    private String status;
    private UUID assigneeId;
    private UUID assigneeDepartmentId;
    private UUID reporterId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp dueDate;
}
