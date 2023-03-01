package org.unicrm.ticket.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(schema = "tickets_schema", name = "ticket_departments")
public class TicketDepartment {

    @Id
    @Column(name = "department_id")
    @JsonSerialize(using = UUIDSerializer.class)
    private Long id;

    @Column(name = "department_name")
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketDepartment)) return false;
        TicketDepartment that = (TicketDepartment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

