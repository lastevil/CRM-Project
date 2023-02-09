import org.apache.http.util.Asserts;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unicrm.analytic.converter.DepartmentMapper;
import org.unicrm.analytic.converter.TicketMapper;
import org.unicrm.analytic.converter.UserMapper;
import org.unicrm.analytic.dto.UserFrontDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.Ticket;
import org.unicrm.analytic.entities.User;
import org.unicrm.lib.dto.TicketDto;
import org.unicrm.lib.dto.UserDto;

import java.util.UUID;
@SpringBootTest(classes = {UserMapper.class, UserFrontDto.class, User.class, Department.class, UserDto.class})
public class UserMapperTest {
    @Test
    public void convertFromUserDto() {
        UserDto userDto = new UserDto();
        UUID id = UUID.randomUUID();
        userDto.setId(id);
        userDto.setFirstName("FirstName");
        userDto.setLastName("LastName");
        Long depId = 5L;
        userDto.setDepartmentId(depId);
        userDto.setDepartmentTitle("TestDepartment");

        Department department = DepartmentMapper.INSTANCE.fromUserDto(userDto);

        User user = UserMapper.INSTANCE.fromUserDto(userDto, department);
        System.out.println("Test 1:");
        System.out.println(userDto);
        System.out.println(user);
        Asserts.notNull(user, "user is empty!");
        Asserts.check(user.getId().equals(userDto.getId()), "wrong id");
        Asserts.check(user.getDepartment().equals(department), "wrong department");
        Asserts.check(user.getFirstName().equals(userDto.getFirstName()), "wrong FirstName");
        Asserts.check(user.getLastName().equals(userDto.getLastName()), "wrong FirstName");
    }

    @Test
    public void convertEntityToFrontDto() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("First");
        user.setLastName("Last");
        user.setDepartment(Department.builder()
                .id(1L).title("Test").build());

        UserFrontDto dto = UserMapper.INSTANCE.fromEntityToFrontDto(user);
        System.out.println("Test 2:");
        System.out.println(user);
        System.out.println(dto);
        Asserts.notNull(dto,"dto is empty!");
        Asserts.check(dto.getFirstName().equals(user.getFirstName()),"wrong FirstName");
        Asserts.check(dto.getLastName().equals(user.getLastName()),"wrong LastName");
        Asserts.check(dto.getId().equals(user.getId()),"wrong ID");
    }

}
