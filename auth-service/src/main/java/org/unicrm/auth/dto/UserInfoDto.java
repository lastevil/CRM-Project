package org.unicrm.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
public class UserInfoDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String departmentTitle;

}
