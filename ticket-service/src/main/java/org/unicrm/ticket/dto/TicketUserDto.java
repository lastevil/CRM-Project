package org.unicrm.ticket.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.*;
import org.unicrm.ticket.entity.TicketDepartment;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketUserDto {

    private UUID id;
    private String firstName;

    private String lastName;

    private TicketDepartment department;
}
