package org.unicrm.analytic.repositorys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unicrm.analytic.entities.Ticket;

import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    @Query(value = "select t from Ticket t where t.assignee.id = ?2 and t.status like ?3")
    Page<Ticket> findAllByAssigneeIdWithStatus(Pageable pageable, UUID id, String status);

    @Query(value = "select t from Ticket t where t.department.id=?2 and t.status like ?3")
    Page<Ticket> findAllByAssigneeDepartmentWithStatus(Pageable pageable, Long id, String status);
}
