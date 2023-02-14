package converters;

import org.apache.http.util.Asserts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.unicrm.analytic.api.Status;
import org.unicrm.analytic.converter.TicketMapper;
import org.unicrm.analytic.dto.TicketFrontDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;
import org.unicrm.lib.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest(classes = {TicketMapper.class, TicketDto.class, Ticket.class, User.class, Department.class})
class TicketMapperTest {

    @Autowired
    User user;
    @Test
    void convertFromTicketDto() {
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

        TicketDto ticketDto = TicketDto.builder()
                .id(UUID.randomUUID()).status(Status.IN_PROGRESS.name()).title("Title")
                .assigneeDepartmentId(assDepartment.getId()).assigneeId(assignee.getId())
                .reporterId(reporter.getId())
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).dueDate(LocalDateTime.now())
                .build();

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
    void convertFromEntityToFrontDto() {
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
        Ticket ticket = Ticket.builder().status(Status.IN_PROGRESS)
                .id(UUID.randomUUID()).assignee(assignee).department(assDepartment)
                .reporter(reporter).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .dueDate(LocalDateTime.now()).build();

        TicketFrontDto dto = TicketMapper.INSTANCE.fromEntityToFrontDto(ticket);

        System.out.println(ticket);
        System.out.println(dto);

        Asserts.check(dto.getId().equals(ticket.getId()), "wrong id");
        Asserts.check(dto.getAssignee().getFirstName().equals(ticket.getAssignee().getFirstName()), "wrong assignee name");
    }
}
