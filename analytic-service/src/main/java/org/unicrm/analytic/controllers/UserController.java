package org.unicrm.analytic.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unicrm.analytic.api.TimeInterval;
import org.unicrm.analytic.dto.GlobalInfo;
import org.unicrm.analytic.dto.UserResponseDto;
import org.unicrm.analytic.services.AnalyticService;
import org.unicrm.analytic.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "Аналитика", description = "Контроллер обработки запросов по пользователям")
public class UserController {
    private final AnalyticService analyticService;
    private final UserService userService;

    @Operation(summary = "метод получения сторудников отдела")
    @GetMapping("/department/{id}")
    public List<UserResponseDto> getDepartmentEmployees(@PathVariable Long id) {
        return userService.getUsersFromDepartment(id);
    }
    @Operation(summary = "Метод получения пользователей отдела текущего пользователя")
    @GetMapping("department")
    public List<UserResponseDto> getMyDepartmentEmployees(@RequestHeader String username){
        return userService.getUsersByUserDepartment(username);
    }
    @Operation(summary = "метод получения информации о текущем пользователе, за промежуток времени")
    @GetMapping("/info/{interval}")
    public @ResponseBody GlobalInfo getCurrentUserInfo(@PathVariable TimeInterval interval, @RequestHeader String username) {
        return analyticService.getCurrentUserInfo(username, interval);
    }
    @Operation(summary = "метод, для получения общей информации, о деятельности сотрудника, за промежуток времени")
    @GetMapping("/{id}/info/{interval}")
    public @ResponseBody GlobalInfo getUserInformation(@PathVariable UUID id, @PathVariable TimeInterval interval) {
        return analyticService.getUserInfoById(id, interval);
    }
}
