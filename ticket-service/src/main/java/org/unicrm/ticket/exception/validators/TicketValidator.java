package org.unicrm.ticket.exception.validators;

import org.springframework.stereotype.Component;
import org.unicrm.ticket.dto.TicketRequestDto;
import org.unicrm.ticket.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class TicketValidator {
    public void validateCreation(TicketRequestDto ticketDto, Long departmentId, UUID assigneeId, String username){
        List<String> errors = new ArrayList<>();

        if (ticketDto.getTitle().isBlank() || ticketDto.getTitle() == null){
            errors.add("Заголовок задачи не может быть пустым");
        }
        if(ticketDto.getStatus() == null) {
            errors.add("Статус заявки не может быть null");
        }
        if(ticketDto.getDueDate() == null) {
            errors.add("Дата выполнения не может быть null");
        }
        if(departmentId==null) {
            errors.add("id департамента не может быть null");
        }
        if (assigneeId==null){
            errors.add("id исполнителя не может быть null");
        }
        if(username.isBlank() || username == null) {
            errors.add("Имя пользователя не может быть null");
        }
        if (!errors.isEmpty()){
            throw new ValidationException(errors);
        }
    }
}

