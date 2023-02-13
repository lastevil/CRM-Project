import org.apache.http.util.Asserts;
import org.junit.jupiter.api.Test;
import org.unicrm.analytic.converter.DepartmentMapper;
import org.unicrm.analytic.dto.DepartmentFrontDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.lib.dto.UserDto;

import java.util.UUID;

public class DepartmentMapperTest {
    @Test
    public void convertFromUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setFirstName("FirstName");
        userDto.setLastName("LastName");
        userDto.setDepartmentId(9L);
        userDto.setDepartmentTitle("TestDepartment");

        Department department = DepartmentMapper.INSTANCE.fromUserDto(userDto);

        System.out.println("Test 1:");
        System.out.println(userDto);
        System.out.println(department);

        Asserts.notNull(department,"department is null");
        Asserts.check(userDto.getDepartmentId().equals(department.getId()),"wrong id");
        Asserts.check(userDto.getDepartmentTitle().equals(department.getTitle()),"wrong name");
    }

    @Test
    public void convertFromEntityToFrontDto(){
        Department department = new Department(1L,"Test");

        DepartmentFrontDto dto = DepartmentMapper.INSTANCE.fromEntityToFrontDto(department);

        System.out.println("Test 2:");
        System.out.println(department);
        System.out.println(dto);

        Asserts.notNull(dto,"dto is null!");
        Asserts.check(dto.getId().equals(department.getId()),"wrong id");
        Asserts.check(dto.getTitle().equals(department.getTitle()),"wrong department name");
    }
}
