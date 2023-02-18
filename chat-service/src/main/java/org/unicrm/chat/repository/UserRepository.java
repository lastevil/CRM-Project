package org.unicrm.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unicrm.chat.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findByUuidNot(UUID uuid);

    Optional<User> findByUserName(String userName);
    @Query(value = "select u from User u where u.group.id=:groupId")
    List<User> findByGroupsId(Long groupId);

    Optional<User> findById(UUID id);
}
