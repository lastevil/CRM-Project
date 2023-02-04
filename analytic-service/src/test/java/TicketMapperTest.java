import org.apache.http.util.Asserts;
import org.junit.jupiter.api.Test;
import org.unicrm.analytic.converter.TicketMapper;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;
import org.unicrm.lib.dto.TicketDto;

import java.sql.Timestamp;
import java.util.UUID;


public class TicketMapperTest {
    @Test
    public void transformFromTicketDto() {
        User assignee = new User();
        User reporter = new User();
        Department assDepartment = new Department();
        Department repDepartment = new Department();
        assDepartment.setName("assDepartment");
        assDepartment.setId(7L);
        repDepartment.setId(3L);
        repDepartment.setName("repDepartment");

        assignee.setId(UUID.randomUUID());
        assignee.setFirstName("FirstTest");
        assignee.setLastName("Assigner");
        assignee.setDepartment(assDepartment);

        reporter.setId(UUID.randomUUID());
        reporter.setFirstName("SecondTest");
        reporter.setLastName("Reporter");
        reporter.setDepartment(repDepartment);

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

        Ticket ticket = TicketMapper.INSTANCE.fromTicketDto(ticketDto,reporter,assignee,assDepartment);
        System.out.println(ticketDto);
        System.out.println(ticket);
        Asserts.check(ticket.getId().equals(ticketDto.getId()),"wrong id");
        Asserts.check(ticket.getAssignee().equals(assignee),"wrong assignee");
        Asserts.check(ticket.getReporter().equals(reporter),"wrong reporter");
        Asserts.check(ticket.getDepartment().equals(assDepartment),"wrong department");
        Asserts.check(ticket.getTitle().equals(ticketDto.getTitle()),"wrong title");
        Asserts.check(ticket.getCreatedAt().equals(ticketDto.getCreatedAt()),"wrong created time");
        Asserts.check(ticket.getDueDate().equals(ticketDto.getDueDate()),"wrong due time");
        Asserts.check(ticket.getUpdatedAt().equals(ticketDto.getUpdatedAt()),"wrong update time");

    }
}
