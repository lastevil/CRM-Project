package org.unicrm.analytic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unicrm.analytic.api.TimeInterval;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentPage {
    @Schema(description = "Времянной промежуток", example = "DAY, WEEK, MONTH, THREE_MONTH, HALF_YEAR, YEAR")
    TimeInterval timeInterval;
    @Schema(description = "Номер страницы", example = "1")
    int page;
    @Schema(description = "Колличество отобрадаемых элементов", example = "10")
    int countElements;
}
