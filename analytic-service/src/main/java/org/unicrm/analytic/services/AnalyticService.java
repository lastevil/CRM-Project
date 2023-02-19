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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        List<Ticket> ticketList = ticketService.findAllTicketByAssignee(user.getId(), time);
        GlobalInfo userInfo = calculateInformation(ticketList);
        userInfo.setUserId(user.getId());
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        return userInfo;
      }

    @Transactional
    public GlobalInfo getDepartmentInfo(Long departmentId, TimeInterval time) {
        List<Ticket> ticketList = ticketService.findAllTicketByAssigneeDepartment(departmentId, time);
        GlobalInfo departmentInfo = calculateInformation(ticketList);
        departmentInfo.setDepartmentId(departmentId);
        departmentInfo.setDepartmentTitle(departmentService.findById(departmentId).getTitle());
        return departmentInfo;
    }

    public GlobalInfo getCurrentUserInfo(String username, TimeInterval interval) {
        User user = userService.findByUsername(username);
        return getUserInfo(user, interval);
    }

    public GlobalInfo getUserInfoById(UUID userId, TimeInterval interval) {
        User user = userService.findById(userId);
        return getUserInfo(user, interval);
    }

    private GlobalInfo calculateInformation(List<Ticket> ticketList) {
        GlobalInfo info = new GlobalInfo();
        info.setTicketCount(ticketList.size());
        info.setTicketCountAccepted(ticketList.stream().filter(t -> t.getStatus().equals(Status.ACCEPTED)).count());
        info.setTicketBacklogCount(ticketList.stream().filter(t -> t.getStatus().equals(Status.BACKLOG)).count());
        info.setTicketCountDone(ticketList.stream().filter(t -> t.getStatus().equals(Status.DONE)).count());
        info.setTicketCountInProgress(ticketList.stream().filter(t -> t.getStatus().equals(Status.IN_PROGRESS)).count());
        info.setTicketCountOverdue(ticketList.stream().filter(t -> t.getOverdue().equals(Status.OVERDUE)).count());
        Integer kpi = 0;
        if (info.getTicketCount() != 0) {
            Double calculate = (((info.getTicketCountAccepted() + (info.getTicketCountDone() * DONE_INDEX)
                    + (info.getTicketCountInProgress() * IN_PROGRESS_INDEX)) / info.getTicketCount()) * 100)
                    - info.getTicketCountOverdue();
            kpi = calculate.intValue();
        }
        info.setKpi(kpi);
        return info;
    }
}