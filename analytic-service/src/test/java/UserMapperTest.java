import org.apache.http.util.Asserts;
import org.junit.jupiter.api.Test;
import org.unicrm.analytic.converter.DepartmentMapper;
import org.unicrm.analytic.converter.UserMapper;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.User;
import org.unicrm.lib.dto.UserDto;

import java.util.UUID;

public class UserMapperTest {
    @Test
    public void transformFromUserDto() {
        UserDto userDto = new UserDto();
        UUID id = UUID.randomUUID();
        userDto.setId(id);
        userDto.setFirstName("FirstName");
        userDto.setLastName("LastName");
        UUID depId = UUID.randomUUID();
        userDto.setDepartmentId(depId);
        userDto.setDepartment("TestDepartment");

        Department department = DepartmentMapper.INSTANCE.fromUserDto(userDto);

        User user = UserMapper.INSTANCE.fromUserDto(userDto,department);
        System.out.println(userDto);
        System.out.println(user);
        Asserts.notNull(user,"user is empty");
        Asserts.check(user.getId().equals(userDto.getId()),"wrong id");
        Asserts.check(user.getDepartment().equals(department),"wrong department");
        Asserts.check(user.getFirstName().equals(userDto.getFirstName()),"wrong FirstName");
        Asserts.check(user.getLastName().equals(userDto.getLastName()),"wrong FirstName");
    }

}
