package org.unicrm.analytic.exceptions.validators;

import org.springframework.stereotype.Component;
import org.unicrm.analytic.dto.kafka.KafkaUserDto;
import org.unicrm.analytic.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;
@Component
public class UserValidator {
    List<String> errors  = new ArrayList<>();

    public void validate(KafkaUserDto userDto) {
        if (userDto.getId() == null) {
            errors.add("id пользователя не может быть пустым");
        }
        if (userDto.getFirstName()==null){
            errors.add("Имя пользователя не может быть пустым");
        }
        if (userDto.getLastName()==null){
            errors.add("Фамилия пользователя не может быть пустым");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
