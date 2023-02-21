package org.unicrm.ticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unicrm.ticket.dto.TicketDepartmentDto;
import org.unicrm.ticket.services.TicketDepartmentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Отделы сервиса 'Заявки'", description = "Контроллер для обработки запросов с front-service на получение списка отделов")
@RequestMapping("/api/v1/departments")
public class DepartmentController {
    private final TicketDepartmentService departmentService;

    @Operation(summary = "метод для получения всех отделов")
    @GetMapping()
    public List<TicketDepartmentDto> getAllDepartments() {
        return departmentService.findAllDepartments();
    }
}
