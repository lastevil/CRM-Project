package org.unicrm.ticket.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(schema = "tickets_schema", name = "ticket_departments")
public class TicketDepartment {

    @Id
    @Column(name = "department_id")
    @JsonSerialize(using = UUIDSerializer.class)
    private Long departmentId;

    @Column(name = "department_name")
    private String title;
}

