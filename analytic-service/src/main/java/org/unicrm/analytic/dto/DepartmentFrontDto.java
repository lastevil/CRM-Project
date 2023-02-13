package org.unicrm.analytic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class DepartmentFrontDto {
    @Schema(description = "id департамента",example = "1L")
    private Long id;

    @Schema(description = "Название отдела",example = "Бухгалтерия")
    private String title;
}
