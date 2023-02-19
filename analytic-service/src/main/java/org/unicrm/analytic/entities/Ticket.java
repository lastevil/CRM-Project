package org.unicrm.analytic.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.*;
import org.unicrm.analytic.api.Status;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket {
    @Id
    @Column(name = "id")
    @JsonSerialize(using = UUIDSerializer.class)
    private UUID id;
    @Column(name = "title")
    private String title;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    @Column(name = "overdue_status")
    @Enumerated(EnumType.STRING)
    private Status overdue;

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", overdueStatus='" + overdue +'\''+
                ", assigneeId='" + assignee.getId() + '\'' +
                ", departmentId='" + department.getId() + '\'' +
                ", reporterId='" + reporter.getId() + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", dueDate= " + dueDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
