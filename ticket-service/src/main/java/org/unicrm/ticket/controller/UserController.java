package org.unicrm.ticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unicrm.ticket.dto.UserDto;
import org.unicrm.ticket.services.TicketUserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Пользователи сервиса 'Заявки'", description = "Контроллер для обработки запросов с front-service на получение списка пользователей")
@RequestMapping("/api/v1/users")
public class UserController {

    private final TicketUserService userService;

    @Operation(summary = "метод для получения всех пользователей")
    @GetMapping("/tickets/users")
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers();
    }

    @Operation(summary = "метод для получения пользователей по конкретному отделу")
    @GetMapping("tickets/users/{departmentId}")
    public List<UserDto> getAllUsersFromDepartment(Long departmentId) {
        return userService.findAllUsersByDepartments(departmentId);
    }
}
