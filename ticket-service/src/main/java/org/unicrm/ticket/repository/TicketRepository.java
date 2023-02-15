package org.unicrm.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.unicrm.ticket.entity.Ticket;
import org.unicrm.ticket.entity.TicketUser;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Query(value = "select t from Ticket t where t.assigneeId = :assigneeId")
    List<Ticket> findAllByAssignee(TicketUser assigneeId);

    //TODO: Fix
    @Query(value = "select t from Ticket t where t.departmentId = :departmentId")
    List<Ticket> findAllByDepartment(Long departmentId);

    @Query(value = "select t from Ticket t where t.assigneeId = :assigneId and t.status = :status")
    List<Ticket> findAllByAssigneeIdAndStatus(UUID assigneId, String status);

    @Query(value = "select count(*) from Ticket t where t.departmentId = :departmentId and t.status = :status")
    Integer countAllByDepartmentAndStatus(Long departmentId, String status);

    @Query(value = "select count(*) from Ticket t where t.assigneeId = :assigneeId and t.status = :status")
    Integer countAllByAssigneeIdAndStatus(UUID assigneeId, String status);

    @Query(value = "select count(*) from Ticket t where t.reporterId = :reporterId and t.status = :status")
    Integer countAllByReporterIdAndStatus(UUID reporterId, String status);
}
