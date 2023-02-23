package org.unicrm.analytic.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unicrm.analytic.api.OverdueStatus;
import org.unicrm.analytic.api.Status;
import org.unicrm.analytic.entities.Ticket;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    @Query(value = "select t from Ticket t where t.assignee.id = :userId and t.status = :status and t.createdAt between :beginTime and :endTime")
    List<Ticket> findAllByAssigneeIdWithStatus(UUID userId, Status status, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "select t from Ticket t where t.department.id=:departmentId and t.status = :status and t.createdAt between :beginTime and :endTime")
    List<Ticket> findAllByAssigneeDepartmentWithStatus(Long departmentId, Status status, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "select t from Ticket t where t.assignee.id = :userId and t.createdAt between :beginTime and :endTime")
    List<Ticket> findAllByAssigneeId(UUID userId, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "select t from Ticket t where t.department.id=:departmentId and t.createdAt between :beginTime and :endTime")
    List<Ticket> findAllByDepartmentAssigneeId(Long departmentId, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "SELECT count(t) from Ticket t where t.assignee.id=:userId and t.dueDate between :beginTime and :endTime")
    Optional<Long> countByAssigneeAndDueDateBetween(UUID userId, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "SELECT count(t) from Ticket t where t.department.id=:departmentId and t.dueDate between :beginTime and :endTime")
    Optional<Long> countByDepartmentAndDueDateBetween(Long departmentId, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "select count(t) from Ticket t where t.status=:status and t.assignee.id=:userId and t.dueDate between :beginTime and :endTime group by t.status")
    Optional<Long> countByStatusAndAssigneeGroupByStatus(UUID userId, Status status, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "select count(t) from Ticket t where t.overdue=:status and t.assignee.id=:userId and t.dueDate between :beginTime and :endTime group by t.overdue")
    Optional<Long> countByOverdueAndAssigneeGroupByOverdue(UUID userId, OverdueStatus status, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "select count(t) from Ticket t where t.status=:status and t.department.id=:departmentId and t.dueDate between :beginTime and :endTime group by t.status")
    Optional<Long> countByStatusAndDepartmentGroupByStatus(Long departmentId, Status status, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "select count(t) from Ticket t where t.overdue=:status and t.department.id=:departmentId and t.dueDate between :beginTime and :endTime group by t.overdue")
    Optional<Long> countByOverdueAndDepartmentGroupByOverdue(Long departmentId, OverdueStatus status, LocalDateTime beginTime, LocalDateTime endTime);
}