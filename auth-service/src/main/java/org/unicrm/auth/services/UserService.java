package org.unicrm.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.auth.dto.UpdatedUserDto;
import org.unicrm.auth.dto.UserRegDto;
import org.unicrm.auth.entities.Role;
import org.unicrm.auth.entities.Status;
import org.unicrm.auth.entities.User;
import org.unicrm.auth.exceptions.ResourceNotFoundException;
import org.unicrm.auth.mappers.EntityDtoMapper;
import org.unicrm.auth.repositories.UserRepository;
import org.unicrm.lib.dto.UserDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final DepartmentService departmentService;
    private final RoleService roleService;
    private final KafkaTemplate<UUID, UserDto> kafkaTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(EntityDtoMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto getByUsername(String username) {
        return EntityDtoMapper.INSTANCE.toDto(findByUsername(username));
    }

    @Transactional
    public void saveNewUser(UserRegDto userRegDto) {
        User user = EntityDtoMapper.INSTANCE.toEntity(userRegDto);
        String[] username = userRegDto.getEmail().split("@");
        user.setUsername(username[0]);
        user.setPassword(passwordEncoder.encode(userRegDto.getPassword()));
        Role roleUser = roleService.findRoleByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);
        user.setRoles(userRoles);
        user.setStatus(Status.NOT_ACTIVE);
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(UpdatedUserDto updatedUserDto) {
        User user = findByUsername(updatedUserDto.getUsername());
        System.out.println("test");
        if (updatedUserDto.getEmail() != null) {
            user.setEmail(updatedUserDto.getEmail());
        }
        if (updatedUserDto.getUsername() != null) user.setFirstName(updatedUserDto.getFirstName());
        if (updatedUserDto.getLastName() != null) user.setLastName(updatedUserDto.getLastName());
        if (updatedUserDto.getPassword() != null) user.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        userRepository.save(user);
        kafkaTemplate.send("userTopic", UUID.randomUUID(), EntityDtoMapper.INSTANCE.toDto(user));
    }

    @Transactional
    public void changeLogin(String username, String login) {
        User user = findByUsername(username);
        user.setUsername(login);
        kafkaTemplate.send("userTopic", UUID.randomUUID(), EntityDtoMapper.INSTANCE.toDto(user));
    }

    @Transactional
    public void userVerification(String username, Status status, String departmentTitle) {
        User user = findByUsername(username);
        if (status != null) {
            try {
                user.setStatus(status);
            } catch (IllegalArgumentException e) {
                throw new ResourceNotFoundException("incorrect status selected");
            }
        }
        if (departmentTitle != null) user.setDepartment(departmentService.findDepartmentByTitle(departmentTitle));
        kafkaTemplate.send("userTopic", UUID.randomUUID(), EntityDtoMapper.INSTANCE.toDto(user));
    }

    @Transactional
    public void addRole(String username, String roleName) {
        User user = findByUsername(username);
        user.getRoles().add(roleService.findRoleByName(roleName));
    }

    private User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException(String.format("User '%s' not found", username));
        }
        return user;
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAllByStatusEqualsNoActive() {
        return userRepository.findAllByStatusEquals(Status.NOT_ACTIVE).stream().map(EntityDtoMapper.INSTANCE::toDto).collect(Collectors.toList());
    }
}