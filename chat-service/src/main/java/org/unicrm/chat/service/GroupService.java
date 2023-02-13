package org.unicrm.chat.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.unicrm.chat.entity.Group;
import org.unicrm.chat.repository.GroupRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    public Optional<Group> findById(Long id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Group '%s' not found", id));
        }
        return group;
    }

    public List<Group> findByUsersId(UUID id){
        return groupRepository.findByUsers_Uuid(id);
    }
}
