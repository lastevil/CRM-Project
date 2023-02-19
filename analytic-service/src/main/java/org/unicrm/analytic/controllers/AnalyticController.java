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
import org.unicrm.analytic.services.DepartmentService;
import org.unicrm.analytic.services.TicketService;
import org.unicrm.analytic.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Аналитика", description = "Контроллер обработки запросов по статистике")
public class AnalyticController {
    private final AnalyticService service;
    private final TicketService ticketService;
    private final DepartmentService departmentService;
    private final UserService userService;

    @Operation(summary = "метод, для получения страниц с задачами пользователя, с определенным статусом за промежуток времени")
    @PostMapping("/user/{id}/tickets/{status}")
    public Page<TicketFrontDto> getUserTicketsForTheTimeWithStatus(@PathVariable UUID id, @PathVariable Status status, @RequestBody CurrentPage information) {
        return ticketService.getTicketByAssignee(id, status, information);
    }

    @Operation(summary = "метод, для получения страниц с задачами отдела, с определенным статусом за промежуток времени")
    @PostMapping("/department/{id}/tickets/{status}")
    public Page<TicketFrontDto> getDepartmentTicketsForTheTimeWithStatus(@PathVariable Long id, @PathVariable Status status, @RequestBody CurrentPage page) {
        return ticketService.getTicketByAssigneeDepartment(id, status, page);
    }

    @Operation(summary = "метод получения страниц с сторудниками отдела")
    @GetMapping("/department/{id}/users")
    public Page<UserFrontDto> getDepartmentEmployees(@PathVariable Long id, @RequestBody CurrentPage page) {
        return userService.getUsersFromDepartment(id, page);
    }

    @Operation(summary = "метод получения списка отделов организации")
    @GetMapping("/departments")
    public @ResponseBody List<DepartmentFrontDto> getDepartments() {
        return departmentService.getDepartments();
    }

    @Operation(summary = "метод, для получения общей информации, о деятельности сотрудника, за промежуток времени")
    @GetMapping("/user/{id}/info/{interval}")
    public @ResponseBody GlobalInfo getUserInformation(@PathVariable UUID id, @PathVariable TimeInterval interval) {
        return service.getUserInfo(id, interval);
    }

    @Operation(summary = "метод, для получения общей информации, о деятельности отдела, за промежуток времени")
    @GetMapping("/department/{id}/{interval}")
    public @ResponseBody GlobalInfo getDepartmentInfo(@PathVariable Long id, @PathVariable TimeInterval interval) {
        return service.getDepartmentInfo(id, interval);
    }
}