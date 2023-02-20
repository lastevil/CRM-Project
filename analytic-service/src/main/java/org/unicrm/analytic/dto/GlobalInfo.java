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
    @Schema(description = "Колличество задач по статусам", example = "Выполнено:1")
    Map<Status, Integer> mapTicketsStatusCount;
    @Schema(description = "Показатель результативности", example = "75")
    private Integer kpi;
}


//    @Schema(description = "Количество задач в работе", example = "52")
//    private Long ticketCountInProgress;
//    @Schema(description = "Количество новых задач", example = "2")
//    private Long ticketBacklogCount;
//    @Schema(description = "Количество выполненых задач", example = "21")
//    private Long ticketCountDone;
//    @Schema(description = "Количество завершенных задач", example = "12")
//    private Long ticketCountAccepted;
//    @Schema(description = "Количество просроченых задач", example = "5")
//    private Long ticketCountOverdue;