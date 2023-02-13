package org.unicrm.ticket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.*;
import org.unicrm.ticket.entity.TicketDepartment;
import org.unicrm.ticket.entity.TicketStatus;
import org.unicrm.ticket.entity.TicketUser;



import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {

    @JsonSerialize(using = UUIDSerializer.class)
    @JsonDeserialize(using = UUIDDeserializer.class)
    private UUID id;

    private String title;

    private TicketStatus status;

    private String description;

    @JsonSerialize(contentUsing = UUIDSerializer.class)
    @JsonDeserialize(contentUsing = UUIDDeserializer.class)
    private TicketUser assigneeId;

    @JsonSerialize(contentUsing = UUIDSerializer.class)
    @JsonDeserialize(contentUsing = UUIDDeserializer.class)
    private TicketUser reporterId;

    private TicketDepartment departmentId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;

    private Boolean isOverdue;
}

