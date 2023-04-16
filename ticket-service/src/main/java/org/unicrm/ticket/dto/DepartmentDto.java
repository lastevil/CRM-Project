package org.unicrm.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    @Schema(description = "id отдела", example = "1L")
    private Long id;

    @Schema(description = "Название отдела", example = "QA")
    private String title;
}
