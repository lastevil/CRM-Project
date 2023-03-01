package org.unicrm.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class TicketPage {

    @Min(value = 1)
    @Schema(description = "Индекс страницы со списком заявок", example = "1")
    private int page;

    @Min(value = 10)
    @Schema(description = "Количество заявок для отображения на странице", example = "15")
    private int size;

}
