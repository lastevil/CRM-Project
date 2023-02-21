package org.unicrm.analytic.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unicrm.analytic.api.Status;
import org.unicrm.analytic.entities.Ticket;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    @Query(value = "SELECT count(*) from tickets where assignee_id=:userId and due_date between :beginTime and :endTime", nativeQuery = true)
    Integer countByAssigneeAndDueDateBetween(UUID userId, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "SELECT count(*) from tickets where department_id=:departmentId and due_date between :beginTime and :endTime", nativeQuery = true)
    Integer countByDepartmentAndDueDateBetween(Long departmentId, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "select count(*) from tickets where status=:status and assignee_id=:userId and due_date between :beginTime and :endTime group by status " +
            "union " +
            "select count(*) from tickets where overdue_status=:status and assignee_id=:userId and due_date between :beginTime and :endTime group by overdue_status"
            , nativeQuery = true)
    Integer countTicketAssigneeByStatusDueDateBetweenTime(UUID userId, String status, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "select count(*) from tickets where status=:status and department_id=:departmentId and due_date between :beginTime and :endTime group by status " +
            "union " +
            "select count(*) from tickets where overdue_status=:status and department_id=:departmentId and due_date between :beginTime and :endTime group by overdue_status"
            , nativeQuery = true)
    Integer countTicketDepartmentByStatusDueDateBetweenTime(Long departmentId, String status, LocalDateTime beginTime, LocalDateTime endTime);
}