package org.unicrm.chat.repository;

import org.unicrm.chat.entity.ChatGroup;
import org.unicrm.chat.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAll();

    @Query(value = "select g.id, g.title from users u, groups g, users_groups ug " +
            "where u.id = ug.user_id and g.id = ug.group_id and u.id = :id",
            nativeQuery = true)
    List<Group> findByUsersId(@Param("id") Long userId);
}
