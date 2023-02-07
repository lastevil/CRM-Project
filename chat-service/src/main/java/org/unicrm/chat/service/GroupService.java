package org.unicrm.chat.service;

import org.unicrm.chat.entity.Group;
import org.unicrm.chat.model.ChatMessage;
import org.unicrm.chat.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    public List<Group> findAll(){
        return groupRepository.findAll();
    }

    public List<Group> findByUsersId(ChatMessage chatMessage){
        return groupRepository.findByUsersId(chatMessage.getSenderId());
    }
}
