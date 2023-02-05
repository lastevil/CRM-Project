package org.unicrm.ticket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.*;
import org.unicrm.ticket.entity.TicketDepartment;
import org.unicrm.ticket.entity.TicketStatus;
import org.unicrm.ticket.entity.TicketUser;
import org.unicrm.ticket.serializer.JsonDateSerializer;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {

    @JsonSerialize(using = UUIDSerializer.class)
    private UUID id;

    private String title;
    private TicketStatus status;
    private String description;
    private TicketUser assigneeId;
    private TicketUser reporterId;
    private TicketDepartment departmentId;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Timestamp createdAt;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Timestamp updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;
}

