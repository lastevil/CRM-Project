package org.unicrm.analytic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.unicrm.analytic.dto.CurrentInformation;
import org.unicrm.analytic.api.TimeInterval;
import org.unicrm.analytic.dto.DepartmentFrontDto;
import org.unicrm.analytic.dto.TicketFrontDto;
import org.unicrm.analytic.dto.UserFrontDto;
import org.unicrm.analytic.services.AnalyticService;
import org.unicrm.lib.dto.TicketDto;
import org.unicrm.lib.dto.UserDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/view")
@RequiredArgsConstructor
//@Tag(name = "Аналитика", description = "Контроллер обработки запросов по статистике")
public class AnalyticController {
    //ToDo: интегрировать считывание ролей из заголовков.
    // Определится с необходимость некоторых методов.
    // Определить необходимость дополнительных ДТО.
    // Начинать добавлять автодокументацию.
    // Тестирование контроллера нецелесообразно в виду единственной логики - передача всей логики сервису.
    private final AnalyticService service;

    @GetMapping("/user-tickets")
    public Page<TicketFrontDto> getUserTicketsForTheTimeWithStatus(@RequestBody CurrentInformation information) {
        return service.getTicketByAssignee(information);
    }

    @GetMapping("/department-tickets")
    public Page<TicketFrontDto> getDepartmentTicketsForTheTimeWithStatus(@RequestBody CurrentInformation information) {
        return service.getTicketByAssigneeDepartment(information);
    }

    @GetMapping("/users-of-department")
    public Page<UserFrontDto> getDepartmentEmployees(CurrentInformation information) {
        return service.getUsersFromDepartment(information);
    }

    @GetMapping("/departments")
    public @ResponseBody List<DepartmentFrontDto> getDepartments() {
        return service.getDepartments();
    }

    @GetMapping("/user-info/{id}")
    public @ResponseBody UserFrontDto getUserInformation(@PathVariable UUID id) {
        return service.getUser(id);
    }

    //под вопросом необходимость этих методов!!!
    @PostMapping("/ticket-status")
    public void changeTicketStatus(@RequestBody TicketDto ticketDto) {
        service.updateTicketStatus(ticketDto);
    }

    @PostMapping("/change-user-department")
    public void changeUserDepartment(@RequestBody UserDto userDto) {
        service.changeUserDepartment(userDto);
    }
}