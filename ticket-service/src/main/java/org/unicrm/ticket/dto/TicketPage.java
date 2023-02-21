package org.unicrm.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketPage {

    @Schema(description = "Индекс страницы со списком заявок", example = "1")
    private int page;

    @Schema(description = "Количество заявок для отображения на странице", example = "15")
    private int countElements;
}
