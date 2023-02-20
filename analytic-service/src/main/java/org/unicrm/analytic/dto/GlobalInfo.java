package org.unicrm.analytic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.unicrm.analytic.api.Status;

import java.util.Map;
import java.util.UUID;


@Data
public class GlobalInfo {
    @Schema(description = "id пользователя")
    private UUID userId;
    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;
    @Schema(description = "id отдела", example = "1L")
    private Long departmentId;
    @Schema(description = "Название отдела", example = "Планово-экономический отдел")
    private String departmentTitle;
    @Schema(description = "Количество задач за заданый интервал", example = "300")
    private Integer ticketCount;
    @Schema(description = "Колличество задач по статусам")
    Map<Status, Integer> mapTicketsStatusCount;
    @Schema(description = "Показатель результативности", example = "75")
    private Integer kpi;
}