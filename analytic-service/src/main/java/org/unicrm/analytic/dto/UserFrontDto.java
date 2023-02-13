package org.unicrm.analytic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;
@Data
public class UserFrontDto {
    @Schema(description = "id пользователя", example = "{123123123-3123124453423-7656573451}")
    private UUID id;
    @Schema(description = "Имя", example = "Иван")
    private String firstName;
    @Schema(description = "Фамилия", example = "Иванов")
    private String lastName;
}
