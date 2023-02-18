package org.unicrm.ticket.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketUserDto {

    @JsonSerialize(contentUsing = UUIDSerializer.class)
    @JsonDeserialize(contentUsing = UUIDDeserializer.class)
    @Schema(description = "id юзера", example = "2e18a2f4-a49d-11ed-ad47-0242ac120002")
    private UUID id;


    @Schema(description = "Логин пользователя", example = "a.ivanov")
    private String username;

    @Schema(description = "Имя пользователя", example = "Андрей")

    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;

    @Schema(description = "Отдел, к которому приписан пользователь", example = "QA")
    private TicketDepartmentDto department;
}
