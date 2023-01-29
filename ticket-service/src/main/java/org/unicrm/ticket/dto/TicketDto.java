package org.unicrm.ticket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.unicrm.ticket.entity.TicketStatus;
import org.unicrm.ticket.serializer.JsonDateSerializer;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {

    private Long id;
    private String name;
    private String title;
    private TicketStatus status;
    private String description;
    private Long assigneeId;
    private Long reporterId;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp createdAt;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;
}

