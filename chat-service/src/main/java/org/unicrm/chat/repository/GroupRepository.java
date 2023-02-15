package org.unicrm.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unicrm.chat.entity.Group;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByUsers_Uuid(UUID uuid);

    @Override
    Optional<Group> findById(Long id);
}
