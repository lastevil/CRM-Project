package org.unicrm.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unicrm.chat.entity.Group;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(value = "select g.id, g.title from users u, groups g, users_groups ug " +
            "where u.uuid = ug.user_id and g.id = ug.group_id and u.uuid = :id",
            nativeQuery = true)
    List<Group> findByUsers_Uuid(@Param("id") UUID uuid);

    @Override
    Optional<Group> findById(Long id);
}
