package org.unicrm.analytic.services;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.analytic.api.TimeInterval;
import org.unicrm.analytic.dto.*;
import org.unicrm.analytic.entities.Department;
import org.unicrm.lib.dto.UserDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalyticService {
    private final UserService userService;
    private final DepartmentService departmentService;
    private final TicketService ticketService;

    @KafkaListener(topics = "userTopic", containerFactory = "userKafkaListenerContainerFactory")
    @Transactional
    public void createOrUpdateUserAndDepartment(UserDto userDto) {
        Department department = departmentService.departmentSaveOrUpdate(userDto);
        userService.userSaveOrUpdate(userDto, department);

    }
    @Transactional
    public GlobalInfo getUserInfo(UUID userId, TimeInterval time) {
        return ticketService.getUserInfo(userId,ticketService.getTimeForInterval(time),LocalDateTime.now());
    }
    @Transactional
    public GlobalInfo getDepartmentInfo(Long departmentId, TimeInterval beginTime) {
        return ticketService.getInfoByAssigneeDepartment(departmentId,ticketService.getTimeForInterval(beginTime),LocalDateTime.now());
    }
}