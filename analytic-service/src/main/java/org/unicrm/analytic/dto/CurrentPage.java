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
    @Schema(description = "Временной промежуток")
    private TimeInterval timeInterval;
    @Schema(description = "Номер страницы", example = "1")
    private int page;
    @Schema(description = "Количество отображаемых элементов", example = "10")
    private int countElements;
}
