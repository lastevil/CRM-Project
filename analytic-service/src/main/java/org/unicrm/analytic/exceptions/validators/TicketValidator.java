package org.unicrm.analytic.exceptions.validators;

import org.springframework.stereotype.Component;
import org.unicrm.analytic.dto.kafka.KafkaTicketDto;
import org.unicrm.analytic.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TicketValidator {
    public void validate(KafkaTicketDto ticketDto){
        List<String> errors = new ArrayList<>();

        if (ticketDto.getId()==null){
            errors.add("Id задачи не может быть пустым");
        }
        if (ticketDto.getAssigneeDepartmentId()==null){
            errors.add("id департамента не может равняться 0");
        }
        if (ticketDto.getReporterId()==null){
            errors.add("Id заявителя не может быть пустым");
        }
        if (ticketDto.getStatus().isBlank()){
            errors.add("Статус не может быть пустым");
        }
        if (ticketDto.getCreatedAt()==null){
            errors.add("Время создания заявки не может быть пустым");
        }
        if (ticketDto.getDueDate()==null){
            errors.add("Время срока выполнения заявки не может быть пустым");
        }
        if (!errors.isEmpty()){
            throw new ValidationException(errors);
        }
    }
}
