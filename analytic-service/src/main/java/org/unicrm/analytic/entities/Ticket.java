package org.unicrm.analytic.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
public class Ticket {
    @Id
    @Column(name = "id")
    @JsonSerialize(using = UUIDSerializer.class)
    private UUID id;
    private String title;
    @Column(name = "status")
    private String status;
    @ManyToOne
    @JoinColumn(name = "assigneeId")
    private User assignee;
    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;
    @ManyToOne
    @JoinColumn(name = "reporterId")
    private User reporter;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "due_date")
    private Timestamp dueDate;
}
