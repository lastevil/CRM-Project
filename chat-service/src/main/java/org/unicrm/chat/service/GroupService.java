package org.unicrm.chat.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.chat.entity.Group;
import org.unicrm.chat.entity.User;
import org.unicrm.chat.mapper.GroupMapper;
import org.unicrm.chat.repository.GroupRepository;
import org.unicrm.lib.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public Group findById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Group with id: " + id + " not found"));
        return group;
    }

    public List<Group> findByUsersId(UUID id) {
        return groupRepository.findByUsers_Uuid(id);
    }

    @Transactional
    public Group groupSaveOrUpdate(UserDto dto) {
        Group group;
        if (!groupRepository.existsById(dto.getDepartmentId())) {
            group  = groupMapper.fromGlobalDto(dto);
            return groupRepository.save(group);
        } else {
            group = groupRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Департамент исполнителя не найден в базе"));
            if (dto.getDepartmentTitle() != null) {
                group.setTitle(dto.getDepartmentTitle());
            }
        }
        return group;
    }
}
