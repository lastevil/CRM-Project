package org.unicrm.analytic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;
import org.unicrm.analytic.exceptions.ResourceNotFoundException;
import org.unicrm.analytic.services.utils.AnalyticFacade;
import org.unicrm.lib.dto.TicketDto;
import org.unicrm.lib.dto.UserDto;

@Service
@RequiredArgsConstructor
public class AnalyticService {

    private final AnalyticFacade facade;

    @KafkaListener(topics = "userTopic", containerFactory = "userKafkaListenerContainerFactory")
    private void createUserAndDepartment(UserDto userDto) {
        if (userDto.getId() != null) {
            Department department = facade.getDepartmentMapper().fromUserDto(userDto);
            User user = facade.getUserMapper().fromUserDto(userDto, department);
            if (!facade.getDepartmentRepository().existsById(department.getId())) {
                facade.getDepartmentRepository().save(department);
            }
            if (!facade.getUserRepository().existsById(user.getId())) {
                facade.getUserRepository().save(user);
            }
        }
    }

    @KafkaListener(topics = "ticketTopic", containerFactory = "ticketKafkaListenerContainerFactory")
    private void createTicket(TicketDto ticketDto) {
        Department department = facade.getDepartmentRepository().findById(ticketDto.getAssigneeDepartmentId())
                .orElseThrow(()->new ResourceNotFoundException("Департамент исполнителя не найден в базе"));
        User reporter = facade.getUserRepository().findById(ticketDto.getReporterId())
                .orElseThrow(()->new ResourceNotFoundException("Заявитель не найден в базе"));
        User assignee = facade.getUserRepository().findById(ticketDto.getAssigneeId())
                .orElseThrow(()->new ResourceNotFoundException("Исполнитель не найден в базе"));
        Ticket ticket = facade.getTicketMapper().fromTicketDto(ticketDto, reporter, assignee, department);
        facade.getTicketRepository().save(ticket);
    }
}
