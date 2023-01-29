package org.unicrm.chat.service;

import org.unicrm.chat.entity.Group;
import org.unicrm.chat.entity.User;
import org.unicrm.chat.model.ChatMessage;
import org.unicrm.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String findNickNameById(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) return null;
        return user.get().getNicName();
    }
    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> findByLogin(String sender){
        Optional<User> user = userRepository.findByLogin(sender);
        if (user.isEmpty()) return null;
        return user;
    }
    @Transactional
    public List<User> findAllByNotSenderId(Long senderId){
        return userRepository.findAllByNotSenderId(senderId);
    }

    @Transactional
    public List<User> findByGroupsId(ChatMessage chatMessage){
        return userRepository.findByGroupsId(chatMessage.getGroupId());
    }
//    public Optional<User> findById(Long id){
//        return userRepository.findById(id);
//    }
}
