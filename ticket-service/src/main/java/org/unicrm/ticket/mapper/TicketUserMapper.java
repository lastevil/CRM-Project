package org.unicrm.ticket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.ticket.dto.UserDto;
import org.unicrm.ticket.dto.kafka.KafkaUserDto;
import org.unicrm.ticket.entity.Department;
import org.unicrm.ticket.entity.User;

@Mapper(componentModel = "spring")
public interface TicketUserMapper {
    TicketUserMapper INSTANCE = Mappers.getMapper(TicketUserMapper.class);

    @Mapping(source = "userDto.id", target = "id")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "userDto.status", target = "status")
    User toEntity(UserDto userDto, Department department);

    UserDto toDto(User user);
    
    @Mapping(target = "id", source = "userDto.id")
    @Mapping(target = "department", source = "department")
    @Mapping(source = "status", target = "userDto.status")
    User toEntityfromGlobalDto(KafkaUserDto userDto, Department department);
}
