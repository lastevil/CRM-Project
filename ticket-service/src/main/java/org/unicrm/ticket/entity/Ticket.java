package org.unicrm.ticket.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.unicrm.ticket.serializer.JsonDateSerializer;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tickets", schema = "tickets_schema")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    @Column
    private String description;
    @Column(name = "assignee")
    private Long assigneeId;
    @Column(name = "reporter")
    private Long reporterId;
    @Column(name = "created_at")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp createdAt;
    @Column(name = "updated_at")
    @CreationTimestamp
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp updatedAt;
    @Column(name = "due_date")
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;
}
