package org.unicrm.analytic.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class TicketFrontDto {

    private UUID id;
    private String title;

    private String status;

    private UserFrontDto assignee;

    private DepartmentFrontDto department;

    private UserFrontDto reporter;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime dueDate;
}
