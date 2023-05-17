package org.unicrm.auth.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.auth.dto.DepartmentDto;
import org.unicrm.auth.dto.RoleDto;
import org.unicrm.auth.dto.UserInfoDto;
import org.unicrm.auth.dto.UserRegDto;
import org.unicrm.auth.dto.kafka.KafkaUserDto;
import org.unicrm.auth.entities.Department;
import org.unicrm.auth.entities.Role;
import org.unicrm.auth.entities.User;

@Mapper
public interface EntityDtoMapper {
    EntityDtoMapper INSTANCE = Mappers.getMapper(EntityDtoMapper.class);

    User toEntity(KafkaUserDto kafkaUserDto);

    User toEntity(UserRegDto userRegDto);

    @Mapping(source = "uuid", target = "id")
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.title", target = "departmentTitle")
    KafkaUserDto toDto(User user);

    @Mapping(source = "uuid", target = "id")
    @Mapping(source = "department.title", target = "departmentTitle")
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "username", target = "login")
    UserInfoDto toInfoDto(User user);

    DepartmentDto toDto(Department department);

    Department toEntity(DepartmentDto departmentDto);

    RoleDto toDto(Role role);
}