package org.unicrm.analytic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class GlobalInfo {
    @Schema(description = "Количество задач в работе", example = "2")
    private Integer ticketCountInProgress;
    @Schema(description = "Количество выполненых задач", example = "21")
    private Integer ticketCountDone;
    @Schema(description = "Количество завершенных задач", example = "3")
    private Integer ticketCountAccepted;
    @Schema(description = "Количество просроченых задач", example = "1")
    private Integer ticketCountOverdue;
}
