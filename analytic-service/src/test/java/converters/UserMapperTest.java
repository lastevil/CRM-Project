package converters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.unicrm.analytic.converter.DepartmentMapper;
import org.unicrm.analytic.converter.UserMapper;
import org.unicrm.analytic.dto.UserResponseDto;
import org.unicrm.analytic.dto.kafka.KafkaUserDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.User;

import java.util.UUID;
@SpringBootTest(classes = {UserMapper.class, UserResponseDto.class, User.class, Department.class, KafkaUserDto.class})
class UserMapperTest {
    @Test
    void convertFromUserDto() {
        KafkaUserDto userDto = new KafkaUserDto();
        UUID id = UUID.randomUUID();
        userDto.setId(id);
        userDto.setUsername("login");
        userDto.setFirstName("FirstName");
        userDto.setLastName("LastName");

        Long depId = 5L;
        userDto.setDepartmentId(depId);
        userDto.setDepartmentTitle("TestDepartment");

        Department department = DepartmentMapper.INSTANCE.fromUserDto(userDto);

        User user = UserMapper.INSTANCE.fromUserDto(userDto, department);
        Assertions.assertNotNull(user, "user is empty!");
        Assertions.assertEquals(user.getId(), userDto.getId(), "wrong id");
        Assertions.assertEquals(user.getId(), userDto.getId(), "wrong id");
        Assertions.assertEquals(user.getDepartment(), department, "wrong department");
        Assertions.assertEquals(user.getUsername(), userDto.getUsername(), "wrong username");
        Assertions.assertEquals(user.getFirstName(), userDto.getFirstName(), "wrong FirstName");
        Assertions.assertEquals(user.getLastName(), userDto.getLastName(), "wrong FirstName");
    }

    @Test
    void convertEntityToFrontDto() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("First");
        user.setLastName("Last");
        user.setDepartment(Department.builder()
                .id(1L).title("Test").build());

        UserResponseDto dto = UserMapper.INSTANCE.fromEntityToFrontDto(user);
        Assertions.assertNotNull(dto, "dto is empty!");
        Assertions.assertEquals(dto.getFirstName(), user.getFirstName(), "wrong FirstName");
        Assertions.assertEquals(dto.getLastName(), user.getLastName(), "wrong LastName");
        Assertions.assertEquals(dto.getId(), user.getId(), "wrong ID");
    }

}
