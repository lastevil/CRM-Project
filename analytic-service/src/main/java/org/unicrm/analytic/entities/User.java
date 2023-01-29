package org.unicrm.analytic.entities;

import lombok.Data;

import java.util.UUID;
@Data
public class User {
    UUID id;
    String firstName;
    String lastName;
    String department;
    String role;
}
