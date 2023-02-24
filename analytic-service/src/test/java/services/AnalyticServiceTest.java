package services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.unicrm.analytic.api.OverdueStatus;
import org.unicrm.analytic.api.Status;
import org.unicrm.analytic.api.TimeInterval;
import org.unicrm.analytic.dto.GlobalInfo;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.User;
import org.unicrm.analytic.services.AnalyticService;
import org.unicrm.analytic.services.DepartmentService;
import org.unicrm.analytic.services.TicketService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest(classes = {AnalyticService.class})
class AnalyticServiceTest {
    @Autowired
    private AnalyticService analyticService;
    @MockBean
    private TicketService ticketService;
    @MockBean
    private DepartmentService departmentService;

    @Test
    void getUserInfoTest() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("TestName");
        user.setLastName("TestFamily");
        Mockito.doReturn(4L).when(ticketService).getTicketsCountByAssignee(user.getId(), TimeInterval.DAY);
        Map<String, Long> map = new HashMap<>();
        for (Status s : Status.values()) {
            map.put(s.name(), 0L);
        }
        for (OverdueStatus s : OverdueStatus.values()) {
            map.put(s.name(), 0L);
        }
        map.put(Status.IN_PROGRESS.name(), 2L);
        map.put(Status.ACCEPTED.name(), 2L);
        map.put(OverdueStatus.OVERDUE.name(), 1L);
        Mockito.doReturn(map).when(ticketService).getAssigneeTicketsByStatus(user.getId(), TimeInterval.DAY);

        GlobalInfo globalInfo = analyticService.getUserInfo(user, TimeInterval.DAY);
        Assertions.assertNotNull(globalInfo, "Результат не может быть null");
        Assertions.assertTrue(globalInfo.getKpi() > 0, "В данном примере kpi > 0");
        Assertions.assertNull(globalInfo.getDepartmentTitle(), "департамент должен быть пустым");
        Assertions.assertEquals(2L, globalInfo.getMapTicketsStatusCount().get(Status.IN_PROGRESS.name()), "Неверное значение в мапе");
    }

    @Test
    void getDepartmentInfoTest() {
        Long departmentId = 1L;
        Department department = new Department(1L, "TestDepartment");
        Mockito.doReturn(department).when(departmentService).findById(departmentId);
        Map<String, Long> map = new HashMap<>();
        for (Status s : Status.values()) {
            map.put(s.name(), 0L);
        }
        for (OverdueStatus s : OverdueStatus.values()) {
            map.put(s.name(), 0L);
        }
        map.put(Status.IN_PROGRESS.name(), 2L);
        map.put(Status.ACCEPTED.name(), 2L);
        map.put(Status.BACKLOG.name(), 1L);
        map.put(OverdueStatus.OVERDUE.name(), 2L);
        Mockito.doReturn(map).when(ticketService).getDepartmentTicketsByStatus(departmentId, TimeInterval.DAY);
        Mockito.doReturn(5L).when(ticketService).getTicketsCountByDepartment(departmentId, TimeInterval.DAY);

        GlobalInfo globalInfo = analyticService.getDepartmentInfo(departmentId, TimeInterval.DAY);
        Assertions.assertNotNull(globalInfo, "Результат не может быть null");
        Assertions.assertTrue(globalInfo.getKpi() > 0, "В данном примере kpi > 0");
        Assertions.assertNull(globalInfo.getLastName(), "имя пользователя должно быть null");
        Assertions.assertNotNull(globalInfo.getDepartmentTitle(), "департамент должен быть пустым");
        Assertions.assertEquals(2L, globalInfo.getMapTicketsStatusCount().get(Status.IN_PROGRESS.name()), "Неверное значение в мапе");
    }

}
