package org.unicrm.analytic.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unicrm.analytic.api.Status;
import org.unicrm.analytic.api.TimeInterval;
import org.unicrm.analytic.dto.*;
import org.unicrm.analytic.services.TicketService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Аналитика", description = "Контроллер обработки запросов по задачам")
public class TicketController {
    private final TicketService ticketService;

    @Operation(summary = "метод, для получения страниц с задачами пользователя, с определенным статусом за промежуток времени")
    @PostMapping("/{status}/{interval}/user/{id}")
    public List<TicketResponseDto> getUserTicketsForTheTimeWithStatus(@PathVariable UUID id, @PathVariable Status status, @PathVariable TimeInterval interval) {
        return ticketService.getTicketByAssignee(id, status, interval);
    }

    @Operation(summary = "метод, для получения страниц с задачами отдела, с определенным статусом за промежуток времени")
    @PostMapping("/{status}/{interval}/department/{id}")
    public List<TicketResponseDto> getDepartmentTicketsForTheTimeWithStatus(@PathVariable Long id, @PathVariable Status status, @PathVariable TimeInterval interval) {
        return ticketService.getTicketByAssigneeDepartment(id, status, interval);
    }

}