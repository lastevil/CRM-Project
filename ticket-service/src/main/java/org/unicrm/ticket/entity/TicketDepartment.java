package org.unicrm.ticket.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unicrm.ticket.serializer.JsonDateSerializer;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "tickets_schema", name = "ticket_departments")
public class TicketDepartment {

    @Id
    @Column(name = "department_id")
    @JsonSerialize(using = UUIDSerializer.class)
    private UUID departmentId;

    @Column(name = "department_name")
    private String departmentName;
}

