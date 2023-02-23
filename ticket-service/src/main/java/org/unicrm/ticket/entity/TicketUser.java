package org.unicrm.ticket.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "users_of_ticket")
public class TicketUser {

    @Id
    @Column(name = "id")
    @JsonSerialize(using = UUIDSerializer.class)
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "department_id")
   private TicketDepartment department;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketUser)) return false;
        TicketUser that = (TicketUser) o;
        return getId().equals(that.getId()) && Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getDepartment(), that.getDepartment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getFirstName(), getLastName(), getDepartment());
    }
}
