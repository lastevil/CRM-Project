package org.unicrm.chat.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.chat.dto.LocalUserDto;
import org.unicrm.chat.entity.Group;
import org.unicrm.chat.entity.User;
import org.unicrm.chat.mapper.UserMapper;
import org.unicrm.chat.model.UserRegistration;
import org.unicrm.chat.model.ChatMessage;
import org.unicrm.chat.repository.UserRepository;
import org.unicrm.lib.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GroupService groupService;
    private final UserMapper userMapper;

    public String findNickNameById(UUID id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) return null;
        return user.get().getNickName();
    }
    public List<User> findAll(){
        return userRepository.findAll();
    }

    public List<LocalUserDto> findAllUsers(){
        List<User> users = userRepository.findAll();
        List<LocalUserDto> list = new ArrayList<>();
        for (User u : users) {
            LocalUserDto dto = LocalUserDto.builder()
                    .uuid(u.getUuid())
                    .userName(u.getUserName())
                    .nickName(u.getNickName())
                    .build();
            list.add(dto);
        }
        return list;
    }

    public Optional<User> findByUsername(String sender){
        Optional<User> user = userRepository.findByUserName(sender);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Пользователь '%s' не найден.", sender));
        }
        return user;
    }
    @Transactional
    public List<User> findAllExcludeSender(UUID senderId){
        return userRepository.findByUuidNot(senderId);
    }

    @Transactional
    public List<User> findByGroupsId(ChatMessage chatMessage){
        return userRepository.findByGroupsId(chatMessage.getGroupId());
    }
    @KafkaListener(topics = "userTopic", containerFactory = "userKafkaListenerContainerFactory")
    @Transactional
    public void save(UserDto userReg){
        Optional<User> userOptional = userRepository.findByUserName(userReg.getUsername());
        if (userOptional.isEmpty()) {
            User user = userMapper.toEntity(userReg);
            Optional<Group> group = groupService.findById(1L);
            List<Group> groups = new ArrayList<>();
            if (!group.isEmpty()) {
                groups.add(group.get());
            }
            user.setGroups(groups);
            userRepository.save(user);
        }
    }
}
