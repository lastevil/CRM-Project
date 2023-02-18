package org.unicrm.chat.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.chat.entity.Group;
import org.unicrm.chat.entity.User;
import org.unicrm.chat.mapper.UserMapper;
import org.unicrm.chat.model.ChatMessage;
import org.unicrm.chat.repository.UserRepository;
import org.unicrm.lib.dto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GroupService groupService;
    private final UserMapper userMapper;

    public String findNickNameById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) return null;
        return user.get().getNickName();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String sender) {
        Optional<User> user = userRepository.findByUserName(sender);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Пользователь '%s' не найден.", sender));
        }
        return user;
    }

    @Transactional
    public List<User> findAllByNotSenderId(UUID senderId) {
        return userRepository.findByUuidNot(senderId);
    }

    @Transactional
    public List<User> findByGroupsId(ChatMessage chatMessage) {
        return userRepository.findByGroupsId(chatMessage.getGroupId());
    }

    @KafkaListener(topics = "userTopic", containerFactory = "userKafkaListenerContainerFactory")
    @Transactional
    public void createOrUpdateUserAndGroup(UserDto userDto) {
        Group group = groupService.groupSaveOrUpdate(userDto);
        userSaveOrUpdate(userDto, group);

    }

    @Transactional
    private void userSaveOrUpdate(UserDto dto, Group group) {
        User user;
        if (!userRepository.existsById(dto.getId())) {
            user = userMapper.toEntity(dto);
            user.setGroup(group);
        } else {
            user = userRepository.findById(dto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + dto.getId() + "не найден в базе"));
            if (!user.getGroup().equals(group)) {
                user.setGroup(group);
            }
            if (dto.getFirstName() != null) {
                String[] userName = user.getUserName().split(" ");
                userName[1] = dto.getFirstName();
                user.setUserName(userName[0] + " " + userName[1]);
            }
            if (dto.getLastName() != null) {
                String[] userName = user.getUserName().split(" ");
                userName[0] = dto.getFirstName();
                user.setUserName(userName[0] + " " + userName[1]);
            }
        }
        userRepository.save(user);
    }
}
