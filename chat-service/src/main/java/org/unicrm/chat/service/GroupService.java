package org.unicrm.chat.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.unicrm.chat.entity.Group;
import org.unicrm.chat.model.ChatGroups;
import org.unicrm.chat.model.ListNewUsersGroup;
import org.unicrm.chat.repository.GroupRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Transactional
    public void createGroup(ListNewUsersGroup dto) {
        groupRepository.insert(dto.getTitle());
    }

    public List<ChatGroups> findAll(){
        List<Group> groups = groupRepository.findAll();
        List<ChatGroups> groupsList = new ArrayList<>();
        for (Group g : groups) {
            ChatGroups chatGroups = ChatGroups.builder()
                    .count(0)
                    .id(g.getId())
                    .title(g.getTitle())
                    .build();
            groupsList.add(chatGroups);
        }
        return groupsList;
    }
    @Transactional
    public void addUsers(ListNewUsersGroup dto) {
        int count = groupRepository.findIfUserIsAlreadyInGroup(dto.getUsers(), dto.getId());
        if(count == 0) {
            groupRepository.insertUsers(dto.getUsers(), dto.getId());
        }
    }
    @Transactional
    public void updateTitleGroup(ListNewUsersGroup dto){
        Group group = groupRepository.getById(dto.getId());
        group.setTitle(dto.getTitle());
        groupRepository.save(group);
    }
}

