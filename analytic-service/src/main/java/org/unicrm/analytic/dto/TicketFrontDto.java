package org.unicrm.analytic.dto;

import lombok.Data;


import java.sql.Timestamp;
import java.util.UUID;
@Data
public class TicketFrontDto {

    private UUID id;
    private String title;

    private String status;

    private UserFrontDto assignee;

    private DepartmentFrontDto department;

    private UserFrontDto reporter;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp dueDate;
}
