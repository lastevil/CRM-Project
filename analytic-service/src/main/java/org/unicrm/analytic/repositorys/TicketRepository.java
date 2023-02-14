package org.unicrm.analytic.repositorys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unicrm.analytic.api.Status;
import org.unicrm.analytic.entities.Ticket;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    @Query(value = "select t from Ticket t where t.assignee.id = :id and t.status = :status and t.createdAt between :beginTime and :endTime")
    Page<Ticket> findAllByAssigneeIdWithStatus(Pageable pageable, UUID id, Status status, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "select t from Ticket t where t.department.id=:id and t.status = :status and t.createdAt between :beginTime and :endTime")
    Page<Ticket> countByAssigneeDepartmentWithStatus(Pageable pageable, Long id, Status status, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "select count(t) from Ticket t where t.assignee.id = :id and t.status = :status and t.createdAt between :beginTime and :endTime")
    Integer countByAssigneeIdWithStatus(UUID id, Status status, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "select count(t) from Ticket t where t.department.id= :id and t.status = :status and t.createdAt between :beginTime and :endTime")
    Integer countByAssigneeDepartmentWithStatus(Long id, Status status, LocalDateTime beginTime, LocalDateTime endTime);
}
