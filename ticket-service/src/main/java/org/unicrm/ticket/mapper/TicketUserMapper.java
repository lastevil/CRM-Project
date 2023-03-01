package org.unicrm.ticket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.unicrm.ticket.dto.TicketUserDto;
import org.unicrm.ticket.dto.kafka.KafkaUserDto;
import org.unicrm.ticket.entity.TicketDepartment;
import org.unicrm.ticket.entity.TicketUser;

@Mapper(componentModel = "spring")
public interface TicketUserMapper {
    TicketUserMapper INSTANCE = Mappers.getMapper(TicketUserMapper.class);

    @Mapping(source = "ticketUserDto.id", target = "id")
    @Mapping(source = "department", target = "department")
    TicketUser toEntity(TicketUserDto ticketUserDto, TicketDepartment department);

    TicketUserDto toDto(TicketUser ticketUser);
    
    @Mapping(target = "id", source = "userDto.id")
    @Mapping(target = "department", source = "department")
    TicketUser tofromGlobalDto(KafkaUserDto userDto, TicketDepartment department);
}
