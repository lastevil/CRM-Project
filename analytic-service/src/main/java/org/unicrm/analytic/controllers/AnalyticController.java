package org.unicrm.analytic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unicrm.analytic.dto.CurrentInformation;
import org.unicrm.analytic.entitys.TimeInterval;
import org.unicrm.lib.dto.UserSimpleDto;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AnalyticController {
    /**
     * примерные данные необходимые для таблиц или графиков.
     * 1) колличество выполненых, просроченых и задач в работе отделом, пользователем
     * 1.1) данные за день, неделю, месяц, год?(по дате подачи заявки)
     * 2) время выполнения задач пользователем/отделом
     * 3) своя статистика
     * 4) для руководителей(колличество пользователей всего в отделе активных и неактивных)
     */
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
    public void changeUserDepartment(UserSimpleDto userDto){}
}
