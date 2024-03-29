package org.unicrm.ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unicrm.ticket.entity.TicketStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDto {
    @Schema(description = "title", example="Task")
    String title;
    @Schema(description = "description", example="I need new task")
    String description;

    @Schema(description = "Статус заявки", example = "BACKLOG")
    TicketStatus status;

    @Schema(description="deadline",example="2022-12-25")
    LocalDate dueDate;
}
