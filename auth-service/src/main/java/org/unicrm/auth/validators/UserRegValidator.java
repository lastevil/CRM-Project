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

    private final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
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
