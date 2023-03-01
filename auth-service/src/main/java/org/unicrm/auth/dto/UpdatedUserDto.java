package org.unicrm.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatedUserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
}
