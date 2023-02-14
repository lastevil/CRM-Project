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


    Optional<User> findByUserName(String userName);

    @Modifying
    @Query(value = "insert into users_groups (user_id, group_id) " +
            "values(:user_id, :group_id)",
            nativeQuery = true)
    void insert(@Param("user_id") UUID user_id, @Param("group_id") Long group_id);

    List<User> findByGroupsId(Long groupId);

    Optional<User> findById(UUID id);
}
