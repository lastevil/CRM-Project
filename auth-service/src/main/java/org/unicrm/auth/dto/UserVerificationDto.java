package org.unicrm.auth.dto;

import lombok.Data;
import org.unicrm.auth.entities.Status;

@Data
public class UserVerificationDto {

    private String username;
    private Status status;
    private String departmentTitle;

}
