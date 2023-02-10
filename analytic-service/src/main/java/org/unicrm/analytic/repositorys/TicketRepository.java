package org.unicrm.analytic.repositorys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unicrm.analytic.entities.Ticket;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    @Query(value = "select t from Ticket t where t.assignee.id = ?2 and t.status like ?3 and t.createdAt between ?4 and ?5")
    Page<Ticket> findAllByAssigneeIdWithStatus(Pageable pageable, UUID id, String status, LocalDateTime before, LocalDateTime after);

    @Query(value = "select t from Ticket t where t.department.id=?2 and t.status like ?3 and t.createdAt between ?4 and ?5")
    Page<Ticket> findAllByAssigneeDepartmentWithStatus(Pageable pageable, Long id, String status,LocalDateTime before, LocalDateTime after);
}
