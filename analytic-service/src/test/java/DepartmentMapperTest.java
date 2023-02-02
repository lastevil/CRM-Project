import org.apache.http.util.Asserts;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.unicrm.analytic.converter.DepartmentMapper;
import org.unicrm.analytic.converter.UserMapper;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.User;
import org.unicrm.lib.dto.UserDto;

import java.util.UUID;

public class DepartmentMapperTest {
    @Test
    public void transformFromUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setFirstName("FirstName");
        userDto.setLastName("LastName");
        userDto.setDepartmentId(UUID.randomUUID());
        userDto.setDepartment("TestDepartment");

        Department department = DepartmentMapper.INSTANCE.fromUserDto(userDto);
        System.out.println(userDto);
        System.out.println(department);
        Asserts.check(userDto.getDepartmentId().equals(department.getId()),"wrong id");
        Asserts.check(userDto.getDepartment().equals(department.getName()),"wrong name");
    }
}
