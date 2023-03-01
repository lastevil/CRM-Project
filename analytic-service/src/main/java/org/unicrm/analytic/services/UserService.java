package org.unicrm.analytic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unicrm.analytic.converter.UserMapper;
import org.unicrm.analytic.dto.UserResponseDto;
import org.unicrm.analytic.dto.kafka.KafkaUserDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.User;
import org.unicrm.analytic.exceptions.ResourceNotFoundException;
import org.unicrm.analytic.exceptions.validators.UserValidator;
import org.unicrm.analytic.repositorys.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator validator;

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id: " + id + " не найден"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserResponseDto> getUsersFromDepartment(Long departmentId) {
        return userRepository.findAllByDepartmentId(departmentId).stream()
                .map(userMapper::fromEntityToFrontDto).collect(Collectors.toList());
    }

    public void userSaveOrUpdate(KafkaUserDto dto, Department department) {
        validator.validate(dto);
        if (!userRepository.existsById(dto.getId())) {
            User user = userMapper.fromUserDto(dto, department);
            userRepository.save(user);
        } else {
            User user = findById(dto.getId());
            user.setDepartment(department);
            user.setFirstName(dto.getFirstName());
            user.setLastName(user.getLastName());
            user.setUsername(dto.getUsername());
        }
    }

    public List<UserResponseDto> getUsersByUserDepartment(String username) {
        User user = findByUsername(username);
        return getUsersFromDepartment(user.getDepartment().getId());
    }
}
