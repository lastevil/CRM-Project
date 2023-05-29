package org.unicrm.auth.validators;

import org.springframework.stereotype.Component;
import org.unicrm.auth.dto.UserRegDto;
import org.unicrm.auth.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserRegValidator {

    private final String EMAIL_REGEX = "\\w+@\\w+\\.\\w+";
    private final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public boolean emailValidator(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public void validate(UserRegDto userRegDto) {

        List<String> errors = new ArrayList<>();

        if (userRegDto.getLogin() == null || userRegDto.getLogin().isBlank()) errors.add("Login must not be empty");
        if (userRegDto.getFirstName() == null || userRegDto.getFirstName().isBlank()) errors.add("First name must not be empty");
        if (userRegDto.getLastName() == null || userRegDto.getLastName().isBlank()) errors.add("Last name must not be empty");
        if (userRegDto.getPassword() == null || userRegDto.getPassword().isBlank()) errors.add("Password must not be empty");
        if (userRegDto.getEmail() == null || userRegDto.getEmail().isBlank()) errors.add("Email must not be empty");
        if (!emailValidator(userRegDto.getEmail())) errors.add("The email address is invalid");
        if (!errors.isEmpty()) throw new ValidationException(errors);
    }
}
