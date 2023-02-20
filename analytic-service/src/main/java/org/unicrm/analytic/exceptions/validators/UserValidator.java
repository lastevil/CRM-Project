package org.unicrm.analytic.exceptions.validators;

import org.springframework.stereotype.Component;
import org.unicrm.analytic.exceptions.ValidationException;
import org.unicrm.lib.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
@Component
public class UserValidator {
    List<String> errors  = new ArrayList<>();

    public void validate(UserDto userDto) {
        if (userDto.getId() == null) {
            errors.add("id пользователя не может быть пустым");
        }
        if (userDto.getFirstName()==null){
            errors.add("Имя польлзователя не может быть пустым");
        }
        if (userDto.getLastName()==null){
            errors.add("Фамилия польлзователя не может быть пустым");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
