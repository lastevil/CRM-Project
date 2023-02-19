package org.unicrm.analytic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unicrm.analytic.converter.UserMapper;
import org.unicrm.analytic.dto.UserFrontDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.entities.User;
import org.unicrm.analytic.exceptions.ResourceNotFoundException;
import org.unicrm.analytic.repositorys.UserRepository;
import org.unicrm.lib.dto.UserDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id: " + id + " не найден"));
    }


    public List<UserFrontDto> getUsersFromDepartment(Long departmentId) {
        return userRepository.findAllByDepartmentId(departmentId).stream()
                .map(userMapper::fromEntityToFrontDto).collect(Collectors.toList());
    }

    public void userSaveOrUpdate(UserDto dto, Department department) {
        User user;
        if (!userRepository.existsById(dto.getId())) {
            user = userMapper.fromUserDto(dto, department);
        } else {
            user = userRepository.findById(dto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + dto.getId() + "не найден в базе"));
            user.setDepartment(department);
            if (dto.getFirstName() != null) {
                user.setFirstName(dto.getFirstName());
            }
            if (dto.getLastName() != null) {
                user.setLastName(user.getLastName());
            }
        }
        userRepository.save(user);
    }

    public User findByUsername(String username) {
            return userRepository.findByUsername(username);
    }
}
