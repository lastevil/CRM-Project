package org.unicrm.analytic.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;
@Data
@Entity
public class User {
    @Id
    @Column(name = "id")
    @JsonSerialize(using = UUIDSerializer.class)
    private UUID id;
    @Column(name = "firstName")
    String firstName;
    @Column(name = "lastName")
    String lastName;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
