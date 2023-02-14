package org.unicrm.ticket.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unicrm.ticket.entity.TicketUser;

import java.util.UUID;
@Repository
public interface TicketUserRepository extends JpaRepository<TicketUser, UUID> {
}
