package org.unicrm.analytic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unicrm.analytic.dto.CurrentInformation;
import org.unicrm.analytic.entities.TimeInterval;
import org.unicrm.lib.dto.UserDto;

import java.util.UUID;
/**
 *
 */
@RestController
@RequestMapping("/analytic")
@RequiredArgsConstructor
public class AnalyticController {
    @GetMapping("/userCount")
    public void getActiveUsersCount(){}
    @GetMapping("/userTicketsTimeIntervalWithStatus/{timeInterval}/{status}")
    public void getTicketsForTheTime(CurrentInformation userInfo, @PathVariable TimeInterval timeInterval, @PathVariable String status){};
    @GetMapping("/department/{id}")
    public void getDepartmentInformation(@PathVariable Long id){}
    @GetMapping("/userInfo/{id}")
    public void getUserInformation(@PathVariable String id){}
    @PostMapping("/ticketStatus/{ticketId}/{status}")
    public void changeUserStatus(@PathVariable UUID ticketId, @PathVariable String status){}
    @PostMapping("/userDepartment")
    public void changeUserDepartment(UserDto userDto){}
}
