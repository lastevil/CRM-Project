package org.unicrm.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserRolesDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private List<RoleDto> roles;

}
