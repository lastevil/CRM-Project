package org.unicrm.lib.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private UUID uuid;
    private String username;
    private String firstName;
    private String lastName;
    private Long departmentId;
    private String departmentTitle;

}
