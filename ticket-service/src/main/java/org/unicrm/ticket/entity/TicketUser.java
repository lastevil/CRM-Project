package org.unicrm.ticket.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.*;

import javax.persistence.*;
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
//    @JsonDeserialize(contentUsing = UUIDDeserializer.class)
//    @JsonSerialize(contentUsing = UUIDSerializer.class)
//    @JsonDeserialize(contentUsing = UUIDDeserializer.class)
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
}
