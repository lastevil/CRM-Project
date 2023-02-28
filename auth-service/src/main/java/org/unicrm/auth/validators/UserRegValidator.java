package org.unicrm.auth.validators;

import org.springframework.stereotype.Component;
import org.unicrm.auth.dto.UserRegDto;
import org.unicrm.auth.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRegValidator {

    public void validate(UserRegDto userRegDto) {

        List<String> errors = new ArrayList<>();

        if (userRegDto.getFirstName() == null || userRegDto.getFirstName().isBlank()) errors.add("First name must not be empty");
        if (userRegDto.getLastName() == null || userRegDto.getLastName().isBlank()) errors.add("Last name must not be empty");
        if (userRegDto.getPassword() == null || userRegDto.getPassword().isBlank()) errors.add("Password must not be empty");
        if (userRegDto.getEmail() == null || userRegDto.getEmail().isBlank()) errors.add("Email must not be empty");
        if (!errors.isEmpty()) throw new ValidationException(errors);
    }
}
