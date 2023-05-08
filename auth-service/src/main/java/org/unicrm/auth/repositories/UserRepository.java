package org.unicrm.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unicrm.auth.entities.Status;
import org.unicrm.auth.entities.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAllByStatusEquals (Status status);

    List<User> findAllByStatusEqualsAndUsernameIsNot (Status status, String username);

    @Query(value = "select u from User u where u.username <>:username")
    List<User> findAllExceptLocalAdmin(String username);

    Boolean existsUserByUsername(String username);

    Boolean existsUserByEmail(String email);
}
