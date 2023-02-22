package org.unicrm.analytic.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unicrm.analytic.api.TimeInterval;
import org.unicrm.analytic.dto.DepartmentFrontDto;
import org.unicrm.analytic.dto.GlobalInfo;
import org.unicrm.analytic.services.AnalyticService;
import org.unicrm.analytic.services.DepartmentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/department")
@Tag(name = "Аналитика", description = "Контроллер обработки запросов по департаментам")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final AnalyticService analyticService;

    @Operation(summary = "метод получения списка отделов организации")
    @GetMapping("")
    public @ResponseBody List<DepartmentFrontDto> getDepartments() {
        return departmentService.getDepartments();
    }

    @Operation(summary = "метод, для получения общей информации, о деятельности отдела, за промежуток времени")
    @GetMapping("/{id}/{interval}")
    public @ResponseBody GlobalInfo getDepartmentInfo(@PathVariable Long id, @PathVariable TimeInterval interval) {
        return analyticService.getDepartmentInfo(id, interval);
    }
}
