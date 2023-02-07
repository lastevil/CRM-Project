package org.unicrm.analytic.dto;

import lombok.Data;

import java.util.UUID;
@Data
public class UserFrontDto {
    private UUID id;
    private String firstName;
    private String lastName;
}
