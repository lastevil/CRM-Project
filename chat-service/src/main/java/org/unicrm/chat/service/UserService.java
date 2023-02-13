package org.unicrm.chat.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.chat.entity.Group;
import org.unicrm.chat.entity.User;
import org.unicrm.chat.mapper.UserMapper;
import org.unicrm.chat.mapper.UserRegistration;
import org.unicrm.chat.model.ChatMessage;
import org.unicrm.chat.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GroupService groupService;

    public String findNickNameById(UUID id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) return null;
        return user.get().getNickName();
    }
    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String sender){
        Optional<User> user = userRepository.findByUserName(sender);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Пользователь '%s' не найден.", sender));
        }
        return user;
    }
    @Transactional
    public List<User> findAllByNotSenderId(UUID senderId){
        return userRepository.findByUuidNot(senderId);
    }

    @Transactional
    public List<User> findByGroupsId(ChatMessage chatMessage){
        return userRepository.findByGroupsId(chatMessage.getGroupId());
    }
    @Transactional
    public void save(UserRegistration userReg){
        Optional<User> userOptional = userRepository.findByUserName(userReg.getUserName());
        if (userOptional.isEmpty()) {
            User user = UserMapper.INSTANCE.toEntity(userReg);
            Optional<Group> group = groupService.findById(1L);
            List<Group> groups = new ArrayList<>();
            if (!group.isEmpty()) {
                groups.add(group.get());
            }
            user.builder()
                    .nickName(userReg.getNickName())
                    .userName(userReg.getUserName())
                    .groups(groups)
                    .build();
            userRepository.save(user);
            Optional<User> user1 = findByUsername(userReg.getUserName());
            if (!user1.isEmpty()){
                userRepository.insert(user1.get().getUuid(), 1L);
            }
        }
    }
}
