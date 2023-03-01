package org.unicrm.analytic.exceptions.validators;

import org.springframework.stereotype.Component;
import org.unicrm.analytic.dto.kafka.KafkaUserDto;
import org.unicrm.analytic.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;
@Component
public class DepartmentValidator {
    public void validate(KafkaUserDto userDto){
        List<String> errors = new ArrayList<>();

        if (userDto.getDepartmentTitle().isBlank() || userDto.getDepartmentTitle() == null){
            errors.add("Название департамента не может быть пустым");
        }
        if (userDto.getDepartmentId()==0 || userDto.getDepartmentId()==null){
            errors.add("id департамента не может равняться 0");
        }
        if (!errors.isEmpty()){
            throw new ValidationException(errors);
        }
    }
}
