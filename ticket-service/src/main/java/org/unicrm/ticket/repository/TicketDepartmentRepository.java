package org.unicrm.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unicrm.ticket.entity.TicketDepartment;

@Repository
public interface TicketDepartmentRepository extends JpaRepository<TicketDepartment, Long> {
}
