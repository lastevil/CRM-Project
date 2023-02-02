package org.unicrm.analytic.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "departments")
@Data
public class Department {
    @Id
    @Column(name = "id")
    UUID id;
    @Column(name = "name")
    String name;
}
