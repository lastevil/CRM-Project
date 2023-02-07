package org.unicrm.analytic.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
@Data
@Entity
public class User {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "firstName")
    String firstName;
    @Column(name = "lastName")
    String lastName;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
