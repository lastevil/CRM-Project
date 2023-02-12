package org.unicrm.analytic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TicketFrontDto {
    @Schema(description = "id задачи", example = "{111222-223123123111-321321421}")
    private UUID id;
    @Schema(description = "Заголовок задачи", example = "Помыть посуду")
    private String title;
    @Schema(description = "Статус задачи", example = "В работе")
    private String status;
    @Schema(description = "Исполнитель")
    private UserFrontDto assignee;
    @Schema(description = "Отдел исполнителя")
    private DepartmentFrontDto department;
    @Schema(description = "Заявитель")
    private UserFrontDto reporter;
    @Schema(description = "Время создания")
    private String createdAt;
    @Schema(description = "Время последнего изменения")
    private String updatedAt;
    @Schema(description = "Срок исполнения")
    private String dueDate;
}
