package org.unicrm.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unicrm.ticket.entity.User;

import java.util.List;
import java.util.UUID;
@Repository
public interface TicketUserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);

    @Query(value = "select u.department from User u where u.id = :user")
    Long findUserDepartment(UUID user);

    @Query(value = "select u from User u where u.department.id = :departmentId")
    List<User> findAllByDepartment(Long departmentId);
}
