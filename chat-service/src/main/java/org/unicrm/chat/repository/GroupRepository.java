package org.unicrm.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.unicrm.chat.entity.Group;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {


    @Override
    Optional<Group> findById(Long id);

@Query(value = "select * from groups left join users_groups on id = group_id where user_id=:id", nativeQuery = true)
    List<Group> findByUsers_Uuid(UUID id);
}
