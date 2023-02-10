package org.unicrm.analytic.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.unicrm.analytic.dto.CurrentInformation;
import org.unicrm.analytic.dto.DepartmentFrontDto;
import org.unicrm.analytic.dto.TicketFrontDto;
import org.unicrm.analytic.dto.UserFrontDto;
import org.unicrm.analytic.services.AnalyticService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Аналитика", description = "Контроллер обработки запросов по статистике")
public class AnalyticController {
    private final AnalyticService service;

    //@PreAuthorize("hasAnyAuthority('ROLE_CHIEF', 'ROLE_SUPERVISOR') || #information.userId == authentication.details.id")
    @GetMapping("/user-tickets")
    public Page<TicketFrontDto> getUserTicketsForTheTimeWithStatus(@RequestBody CurrentInformation information) {
        return service.getTicketByAssignee(information);
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_CHIEF', 'ROLE_SUPERVISOR')")
    @GetMapping("/department-tickets")
    public Page<TicketFrontDto> getDepartmentTicketsForTheTimeWithStatus(@RequestBody CurrentInformation information) {
        return service.getTicketByAssigneeDepartment(information);
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_CHIEF', 'ROLE_SUPERVISOR')")
    @GetMapping("/users-of-department")
    public Page<UserFrontDto> getDepartmentEmployees(CurrentInformation information) {
        return service.getUsersFromDepartment(information);
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_CHIEF', 'ROLE_SUPERVISOR')")
    @GetMapping("/departments")
    public @ResponseBody List<DepartmentFrontDto> getDepartments() {
        return service.getDepartments();
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_CHIEF', 'ROLE_SUPERVISOR') || #id == authentication.details.id")
    @GetMapping("/user-info/{id}")
    public @ResponseBody UserFrontDto getUserInformation(@PathVariable UUID id) {
        return service.getUser(id);
    }
}