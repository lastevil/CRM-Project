package org.unicrm.ticket.exception.validators;

import org.springframework.stereotype.Component;
import org.unicrm.ticket.dto.kafka.KafkaUserDto;
import org.unicrm.ticket.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
@Component
public class TicketUserValidator {
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
