package org.unicrm.ticket.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.unicrm.ticket.serializer.JsonDateSerializer;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tickets", schema = "tickets_schema")
public class Ticket {
    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @JsonSerialize(using = UUIDSerializer.class)
    private UUID id;
    @Column
    private String title;
    @Column
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "assignee", columnDefinition = "UUID")
    private TicketUser assigneeId;

    @ManyToOne
    @JoinColumn(name = "reporter", columnDefinition = "UUID")
    private TicketUser reporterId;

    @ManyToOne
    @JoinColumn(name = "department_id", columnDefinition = "UUID")
    private TicketDepartment departmentId;

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
