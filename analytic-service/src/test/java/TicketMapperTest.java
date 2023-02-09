import org.apache.http.util.Asserts;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.unicrm.analytic.converter.DepartmentMapper;
import org.unicrm.analytic.converter.TicketMapper;
import org.unicrm.analytic.converter.UserMapper;
import org.unicrm.analytic.dto.TicketFrontDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;
import org.unicrm.lib.dto.TicketDto;

import java.sql.Timestamp;
import java.util.UUID;

@SpringBootTest(classes = {TicketMapper.class, TicketDto.class, Ticket.class, User.class, Department.class})
public class TicketMapperTest {

    @Test
    public void convertFromTicketDto() {
        Department assDepartment = Department.builder()
                .id(7L).title("assDepartment").build();
        Department repDepartment = Department.builder()
                .id(3L).title("repDepartment").build();
        User assignee = User.builder()
                .id(UUID.randomUUID()).firstName("FirstTest")
                .lastName("Assigner").department(assDepartment)
                .build();
        User reporter = User.builder()
                .id(UUID.randomUUID()).firstName("SecondTest")
                .lastName("Reporter").department(repDepartment)
                .build();

        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(UUID.randomUUID());
        ticketDto.setStatus("Status");
        ticketDto.setTitle("Title");
        ticketDto.setCreatedAt(new Timestamp(4));
        ticketDto.setReporterId(reporter.getId());
        ticketDto.setAssigneeId(assignee.getId());
        ticketDto.setAssigneeDepartmentId(assDepartment.getId());
        ticketDto.setDueDate(new Timestamp(6));
        ticketDto.setUpdatedAt(new Timestamp(1));

        Ticket ticket = TicketMapper.INSTANCE.fromTicketDto(ticketDto, reporter, assignee, assDepartment);
        System.out.println(ticketDto);
        System.out.println(ticket);
        Asserts.check(ticket.getId().equals(ticketDto.getId()), "wrong id");
        Asserts.check(ticket.getAssignee().equals(assignee), "wrong assignee");
        Asserts.check(ticket.getReporter().equals(reporter), "wrong reporter");
        Asserts.check(ticket.getDepartment().equals(assDepartment), "wrong department");
        Asserts.check(ticket.getTitle().equals(ticketDto.getTitle()), "wrong title");
        Asserts.check(ticket.getCreatedAt().equals(ticketDto.getCreatedAt()), "wrong created time");
        Asserts.check(ticket.getDueDate().equals(ticketDto.getDueDate()), "wrong due time");
        Asserts.check(ticket.getUpdatedAt().equals(ticketDto.getUpdatedAt()), "wrong update time");
    }

    @Test
    public void convertFromEntityToFrontDto() {

        Ticket ticket = new Ticket();
        Department assDepartment = Department.builder()
                .id(7L).title("assDepartment").build();
        Department repDepartment = Department.builder()
                .id(3L).title("repDepartment").build();
        User assignee = User.builder()
                .id(UUID.randomUUID()).firstName("FirstTest")
                .lastName("Assigner").department(assDepartment)
                .build();
        User reporter = User.builder()
                .id(UUID.randomUUID()).firstName("SecondTest")
                .lastName("Reporter").department(repDepartment)
                .build();
        ticket.setStatus("test");
        ticket.setId(UUID.randomUUID());
        ticket.setAssignee(assignee);
        ticket.setDepartment(assDepartment);
        ticket.setReporter(reporter);
        ticket.setCreatedAt(new Timestamp(3));
        ticket.setUpdatedAt(new Timestamp(4));
        ticket.setDueDate(new Timestamp(6));

        TicketFrontDto dto = TicketMapper.INSTANCE.fromEntityToFrontDto(ticket);

        System.out.println(ticket);
        System.out.println(dto);

        Asserts.check(dto.getId().equals(ticket.getId()), "wrong id");
        Asserts.check(dto.getAssignee().getFirstName().equals(ticket.getAssignee().getFirstName()), "wrong assignee name");
    }
}
