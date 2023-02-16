package org.unicrm.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unicrm.ticket.entity.TicketDepartment;

public interface TicketDepartmentRepository extends JpaRepository<TicketDepartment, Long> {
}
