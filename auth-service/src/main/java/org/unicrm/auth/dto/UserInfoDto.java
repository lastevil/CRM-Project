package org.unicrm.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.unicrm.auth.entities.Status;

import java.util.UUID;


@Data
@NoArgsConstructor
public class UserInfoDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Long departmentId;
    private String departmentTitle;
    private Status status;

}
