package org.unicrm.auth.validators;

import org.springframework.stereotype.Component;
import org.unicrm.auth.dto.UserVerificationDto;
import org.unicrm.auth.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserVerificationValidator {

    public void validate(UserVerificationDto userVerificationDto) {

        List<String> errors = new ArrayList<>();

        if (userVerificationDto.getUsername() == null) errors.add("Username must not be null");
        if (userVerificationDto.getStatus() == null) errors.add("Status must not be null");
        if (userVerificationDto.getDepartmentTitle() == null) errors.add("DepartmentTitle must not be null");
        if (!errors.isEmpty()) throw new ValidationException(errors);
    }
}
