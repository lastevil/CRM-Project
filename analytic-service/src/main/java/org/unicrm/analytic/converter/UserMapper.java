package org.unicrm.analytic.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.analytic.dto.UserResponseDto;
import org.unicrm.analytic.dto.kafka.KafkaUserDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", source = "userDto.id")
    @Mapping(target = "department", source = "department")
    User fromUserDto(KafkaUserDto userDto, Department department);

    UserResponseDto fromEntityToFrontDto(User user);
}
