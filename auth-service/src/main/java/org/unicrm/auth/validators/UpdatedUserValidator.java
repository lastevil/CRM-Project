package org.unicrm.auth.validators;

import org.springframework.stereotype.Component;
import org.unicrm.auth.dto.UpdatedUserDto;
import org.unicrm.auth.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdatedUserValidator {

    public void validate(UpdatedUserDto updatedUserDto) {

        List<String> errors = new ArrayList<>();

        if (updatedUserDto.getUsername() == null) errors.add("Username must not be null");
        if (updatedUserDto.getFirstName() != null && updatedUserDto.getFirstName().isBlank()) errors.add("First name must not be blank");
        if (updatedUserDto.getLastName() != null && updatedUserDto.getLastName().isBlank()) errors.add("Last name must not be blank");
        if (updatedUserDto.getPassword() != null && updatedUserDto.getPassword().isBlank()) errors.add("Password must not be blank");
        if (updatedUserDto.getEmail() != null && updatedUserDto.getEmail().isBlank()) errors.add("Email must not be blank");
        if (!errors.isEmpty()) throw new ValidationException(errors);
    }
}
