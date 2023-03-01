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
import org.unicrm.analytic.converter.TicketMapper;
import org.unicrm.analytic.exceptions.validators.TicketValidator;
import org.unicrm.analytic.repositorys.TicketRepository;
import org.unicrm.analytic.services.DepartmentService;
import org.unicrm.analytic.services.TicketService;
import org.unicrm.analytic.services.UserService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(classes = {TicketService.class})
public class TicketServiceTest {
    @Autowired
    TicketService ticketService;

    @MockBean
    TicketValidator validator;
    @MockBean
    UserService userService;
    @MockBean
    TicketMapper ticketMapper;
    @MockBean
    DepartmentService departmentService;
    @MockBean
    TicketRepository ticketRepository;

    @Test
    void getTimeForIntervalTest() {
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime afterTimeDay = ticketService.getTimeForInterval(TimeInterval.DAY);
        Assertions.assertEquals(dateTime.getMonth(), afterTimeDay.getMonth(), "Месяц отличается не на заданный интервал");
        Assertions.assertEquals(dateTime.getYear(), afterTimeDay.getYear(), "Год отличается не на заданный интервал");
        Assertions.assertTrue(dateTime.getDayOfMonth() == afterTimeDay.getDayOfMonth() + 1, "Дата отличается не на заданный интервал");
        LocalDateTime afterTimeMonth = ticketService.getTimeForInterval(TimeInterval.MONTH);
        Assertions.assertEquals(dateTime.getMonth(), afterTimeMonth.getMonth().plus(1), "Месяц отличается не на заданный интервал");
        Assertions.assertEquals(dateTime.getYear(), afterTimeMonth.getYear(), "Год отличается не на заданный интервал");
        Assertions.assertEquals(dateTime.getDayOfMonth(), afterTimeMonth.getDayOfMonth(), "Дата отличается не на заданный интервал");
        LocalDateTime afterTimeYear = ticketService.getTimeForInterval(TimeInterval.YEAR);
        Assertions.assertEquals(dateTime.getMonth(), afterTimeYear.getMonth(), "Месяц отличается не на заданный интервал");
        Assertions.assertEquals(dateTime.getYear(), afterTimeYear.getYear() + 1, "Год отличается не на заданный интервал");
        Assertions.assertEquals(dateTime.getDayOfMonth(), afterTimeYear.getDayOfMonth(), "Дата отличается не на заданный интервал");
    }

    @Test
    void getAssigneeTicketsByStatusTest() {
        UUID userID = UUID.randomUUID();
        for (Status s: Status.values()) {
            Mockito.when(ticketRepository.countByStatusAndAssigneeGroupByStatus(userID, s , LocalDateTime.now(), ticketService.getTimeForInterval(TimeInterval.DAY))).thenReturn(Optional.of(0L));
        }
        for (OverdueStatus s: OverdueStatus.values()) {
            Mockito.when(ticketRepository.countByOverdueAndAssigneeGroupByOverdue(userID, s , LocalDateTime.now(), ticketService.getTimeForInterval(TimeInterval.DAY))).thenReturn(Optional.of(0L));
        }
        Map<String, Long> map =ticketService.getAssigneeTicketsByStatus(userID,TimeInterval.DAY);
        Assertions.assertNotNull(map,"Не создается Map");
    }
}
