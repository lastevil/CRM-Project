package org.unicrm.ticket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unicrm.ticket.entity.Ticket;
import org.unicrm.ticket.entity.TicketStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Query(value = "select t from Ticket t")
    Page<Ticket> findAllTickets (Pageable pageable);

    @Query(value = "select t from Ticket t where t.assignee.id = :assignee")
    Page<Ticket> findAllByAssignee(Pageable pageable, UUID assignee);

    @Query(value = "select t from Ticket t where t.department.id = :departmentId")
    Page<Ticket> findAllByDepartment(Pageable pageable, Long departmentId);

    @Query(value = "select t from Ticket t where t.assignee.id = :assigneId and t.status = :status")
    Page<Ticket> findAllByAssigneeIdAndStatus(Pageable pageable, UUID assigneId, TicketStatus status);

    @Query(value = "select * from tickets_schema.tickets where due_date between '2023-02-17T19:51:40.032740' and '2023-02-22T19:51:40.032740' and  (status = 'BACKLOG' or status = 'IN_PROGRESS')"
    , nativeQuery = true)
    List<Ticket> findAllWithStatuses(TicketStatus backlog, TicketStatus inProgress, LocalDateTime before, LocalDateTime after);

    @Query(value = "select * from tickets_schema.tickets where title ilike concat('%', :title, '%') order by updated_at desc", nativeQuery = true)
    Page<Ticket> findTicketsByTitle(Pageable pageable, String title);

    @Query(value = "select t from Ticket t where t.status = :status")
    Page<Ticket> findTicketsByStatus(Pageable pageable, TicketStatus status);
}
