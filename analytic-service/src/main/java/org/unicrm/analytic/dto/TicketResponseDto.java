package org.unicrm.analytic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.unicrm.analytic.api.OverdueStatus;
import org.unicrm.analytic.api.Status;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class TicketResponseDto {
    @Schema(description = "id задачи")
    private UUID id;
    @Schema(description = "Заголовок задачи", example = "Помыть посуду")
    private String title;
    @Schema(description = "Статус задачи", example = "В работе")
    private Status status;
    @Schema(description = "Исполнитель")
    private UserResponseDto assignee;
    @Schema(description = "Отдел исполнителя")
    private DepartmentFrontDto department;
    @Schema(description = "Заявитель")
    private UserResponseDto reporter;
    @Schema(description = "Время создания")
    private OffsetDateTime createdAt;
    @Schema(description = "Время последнего изменения")
    private OffsetDateTime updatedAt;
    @Schema(description = "Срок исполнения")
    private OffsetDateTime dueDate;
    @Schema(description = "Статус срока выполнения задачи")
    private OverdueStatus overdue;
}
