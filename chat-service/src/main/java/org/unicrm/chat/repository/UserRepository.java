package org.unicrm.chat.repository;

import org.unicrm.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    @Query(value = "select * from users u, groups g, users_groups ug " +
            "where u.id = ug.user_id and g.id = ug.group_id and g.id = :group_id",
            nativeQuery = true)
    List<User> findByGroupsId(@Param("group_id") Long groupId);

    @Query(value = "select * from users u where u.id != :id",
            nativeQuery = true)
    List<User> findAllByNotSenderId(@Param("id") Long senderId);


}
