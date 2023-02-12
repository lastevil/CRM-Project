package org.unicrm.analytic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class GlobalInfo {
    @Schema(description = "Колличество задач в работе", example = "2")
    Integer ticketCountInProgress;
    @Schema(description = "Колличество выполненых задач", example = "21")
    Integer ticketCountDone;
    @Schema(description = "Колличество завершенных задач", example = "3")
    Integer ticketCountAccepted;
    @Schema(description = "Колличество просроченых задач", example = "1")
    Integer ticketCountOverdue;
}
