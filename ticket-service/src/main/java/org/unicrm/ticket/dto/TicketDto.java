package org.unicrm.ticket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unicrm.ticket.entity.TicketStatus;


import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {

    @JsonSerialize(using = UUIDSerializer.class)
    @JsonDeserialize(using = UUIDDeserializer.class)
    @Schema(description = "id заявки", example = "cef5b424-a49c-11ed-a8fc-0242ac120002")
    private UUID id;

    @Schema(description = "Заголовок заявки", example = "Провести рефакторинг кода")
    private String title;

    @Schema(description = "Статус заявки", example = "Запланировано")
    private TicketStatus status;

    @Schema(description = "Описание заявки", example = "Требуется провести полный рефакторинг существующего кода")
    private String description;

    @Schema(description = "id исполнителя", example = "2e18a2f4-a49d-11ed-ad47-0242ac120002")
    @JsonSerialize(contentUsing = UUIDSerializer.class)
    @JsonDeserialize(contentUsing = UUIDDeserializer.class)
    private TicketUserDto assigneeId;

    @Schema(description = "id заявителя", example = "34f3d3f0-a49d-11ed-ad47-0242ac120002")
    @JsonSerialize(contentUsing = UUIDSerializer.class)
    @JsonDeserialize(contentUsing = UUIDDeserializer.class)
    private TicketUserDto reporterId;

    @Schema(description = "id отдела", example = "1L")
//    @JsonSerialize(contentUsing = UUIDSerializer.class)
//    @JsonDeserialize(contentUsing = UUIDDeserializer.class)
    private TicketDepartmentDto departmentId;

    @Schema(description = "Дата создания заявки", example = "2023-12-10 14:43")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    //TODO: Fix
    @Schema(description = "Дата обновления заявки", example = "2023-12-10 14:50")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;

    //TODO: Fix
    @Schema(description = "Дата, к которой нужно выполнить заявку", example = "2023-12-11")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;

    //TODO: заменить
    @Schema(description = "Дата, к которой нужно выполнить заявку", example = "2023-12-11")
    private Boolean isOverdue;
}

