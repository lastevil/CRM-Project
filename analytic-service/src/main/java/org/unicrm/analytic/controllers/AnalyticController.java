package org.unicrm.analytic.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.unicrm.analytic.api.Status;
import org.unicrm.analytic.api.TimeInterval;
import org.unicrm.analytic.dto.*;
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
    @Operation(summary = "метод получения страниц с задачами пользователя с определенным статусом за промежуток времяни")
    @GetMapping("/user-tickets/{id}/{status}")
    public Page<TicketFrontDto> getUserTicketsForTheTimeWithStatus(@PathVariable UUID id, @PathVariable Status status, @RequestBody CurrentPage information) {
        return service.getTicketByAssignee(id, status, information);
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_CHIEF', 'ROLE_SUPERVISOR')")
    @Operation(summary = "метод получения страниц с задачами отдела с определенным статусом за промежуток времяни")
    @GetMapping("/department-tickets/{id}/{status}")
    public Page<TicketFrontDto> getDepartmentTicketsForTheTimeWithStatus(@PathVariable Long id, @PathVariable String status, @RequestBody CurrentPage page) {
        return service.getTicketByAssigneeDepartment(id, Status.valueOf(status), page);
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_CHIEF', 'ROLE_SUPERVISOR')")
    @Operation(summary = "метод получения страниц с сторудниками отдела")
    @GetMapping("/users-of-department/{id}")
    public Page<UserFrontDto> getDepartmentEmployees(@PathVariable Long id, @PathVariable CurrentPage page) {
        return service.getUsersFromDepartment(id, page);
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_CHIEF', 'ROLE_SUPERVISOR')")
    @Operation(summary = "метод получения списка отделов организации")
    @GetMapping("/departments")
    public @ResponseBody List<DepartmentFrontDto> getDepartments() {
        return service.getDepartments();
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_CHIEF', 'ROLE_SUPERVISOR') || #id == authentication.details.id")
    @Operation(summary = "метод получения общей информации о деятельности сотрудника за промежуток времяни")
    @GetMapping("/user-info/{id}/{interval}")
    public @ResponseBody GlobalInfo getUserInformation(@PathVariable UUID id, @PathVariable String interval) {
        return service.getUserInfo(id, TimeInterval.valueOf(interval));
    }

    @Operation(summary = "метод получения общей информации о деятельности отдела за промежуток времяни")
    @GetMapping("/{id}/{interval}")
    public @ResponseBody GlobalInfo getDepartmentInfo(@PathVariable Long id, @PathVariable String interval) {
        return service.getDepartmentInfo(id, TimeInterval.valueOf(interval));
    }
}