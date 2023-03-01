package org.unicrm.ticket.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unicrm.ticket.entity.TicketStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDto {

    @JsonSerialize(using = UUIDSerializer.class)
    @JsonDeserialize(using = UUIDDeserializer.class)
    @Schema(description = "id заявки", example = "cef5b424-a49c-11ed-a8fc-0242ac120002")
    private UUID id;

    @Schema(description = "Заголовок заявки", example = "Провести рефакторинг кода")
    private String title;

    @Schema(description = "Статус заявки", example = "Запланировано")
    private TicketStatus status;

    @Schema(description = "Описание заявки", example = "Сделать задачу...")
    private String description;

    @Schema(description = "id исполнителя")
    @JsonSerialize(contentUsing = UUIDSerializer.class)
    @JsonDeserialize(contentUsing = UUIDDeserializer.class)
    private TicketUserDto assignee;

    @Schema(description = "id заявителя")
    @JsonSerialize(contentUsing = UUIDSerializer.class)
    @JsonDeserialize(contentUsing = UUIDDeserializer.class)
    private TicketUserDto reporter;

    @Schema(description = "id отдела")
    private TicketDepartmentDto department;

    @Schema(description = "Дата создания заявки", example = "2023-12-10 14:43")
    private OffsetDateTime createdAt;

    @Schema(description = "Дата, к которой нужно выполнить заявку", example = "2023-12-11")
    private OffsetDateTime dueDate;

    @Schema(description = "Категория просроченности заявки", example = "OVERDUE")
    private TicketStatus overdue;
}
