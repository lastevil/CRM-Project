package org.unicrm.analytic.exceptions.validators;

import org.springframework.stereotype.Component;
import org.unicrm.analytic.exceptions.ValidationException;
import org.unicrm.lib.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
@Component
public class DepartmentValidator {
    public void validate(UserDto userDto){
        List<String> errors = new ArrayList<>();

        if (userDto.getDepartmentTitle().isBlank()){
            errors.add("Название департамента не может быть пустым");
        }
        if (userDto.getDepartmentId()==0){
            errors.add("id департамента не может равняться 0");
        }
        if (!errors.isEmpty()){
            throw new ValidationException(errors);
        }
    }
}
