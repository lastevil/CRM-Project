package org.unicrm.analytic.repositorys;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unicrm.analytic.entities.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(value = "select u from User u where u.department.id=:id")
    List<User> findAllByDepartmentId(Long id);

    User findByUsername(String username);
}
