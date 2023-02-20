package org.unicrm.analytic.services;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.analytic.api.Status;
import org.unicrm.analytic.api.TimeInterval;
import org.unicrm.analytic.dto.*;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;
import org.unicrm.lib.dto.UserDto;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AnalyticService {
    private final UserService userService;
    private final DepartmentService departmentService;
    private final TicketService ticketService;

    private final double DONE_INDEX = 0.5;
    private final double IN_PROGRESS_INDEX = 0.1;

    @KafkaListener(topics = "userTopic", containerFactory = "userKafkaListenerContainerFactory")
    @Transactional
    public void createOrUpdateUserAndDepartment(UserDto userDto) {
        Department department = departmentService.departmentSaveOrUpdate(userDto);
        userService.userSaveOrUpdate(userDto, department);

    }

    @Transactional
    public GlobalInfo getUserInfo(User user, TimeInterval time) {
        GlobalInfo userInfo = new GlobalInfo();
        userInfo.setUserId(user.getId());
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        userInfo.setTicketCount(ticketService.getTicketsCountByAssignee(user.getId(), time));
        return calculateGlobalInformation(userInfo,
                ticketService.getAssigneeTicketsByOverdue(user.getId(), time),
                ticketService.getAssigneeTicketsByStatus(user.getId(), time));
    }

    @Transactional
    public GlobalInfo getDepartmentInfo(Long departmentId, TimeInterval time) {
        GlobalInfo departmentInfo = new GlobalInfo();
        departmentInfo.setTicketCount(ticketService.getTicketsCountByDepartment(departmentId, time));
        departmentInfo.setDepartmentId(departmentId);
        departmentInfo.setDepartmentTitle(departmentService.findById(departmentId).getTitle());
        return calculateGlobalInformation(departmentInfo,
                ticketService.getDepartmentTicketsByOverdue(departmentId, time),
                ticketService.getDepartmentTicketsByStatus(departmentId, time));
    }

    public GlobalInfo getCurrentUserInfo(String username, TimeInterval interval) {
        User user = userService.findByUsername(username);
        return getUserInfo(user, interval);
    }

    public GlobalInfo getUserInfoById(UUID userId, TimeInterval interval) {
        User user = userService.findById(userId);
        return getUserInfo(user, interval);
    }

    private GlobalInfo calculateGlobalInformation(GlobalInfo globalInfo,
                                                  Map<Status, Integer> map1, Map<Status, Integer> map2) {
        Map<Status, Integer> info = Stream.of(map1, map2)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum));
        globalInfo.setMapTicketsStatusCount(info);
        Integer kpi = 0;
        if (globalInfo.getTicketCount() > 0) {
            Double calculate = (((info.get(Status.ACCEPTED) + (info.get(Status.DONE) * DONE_INDEX)
                    + (info.get(Status.IN_PROGRESS) * IN_PROGRESS_INDEX)) / globalInfo.getTicketCount()) * 100)
                    - ((info.get(Status.OVERDUE)));
            kpi = calculate.intValue();
        }
        globalInfo.setKpi(kpi);
        return globalInfo;
    }
}