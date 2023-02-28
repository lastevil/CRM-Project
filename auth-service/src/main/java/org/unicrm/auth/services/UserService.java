package org.unicrm.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.auth.dto.UpdatedUserDto;
import org.unicrm.auth.dto.UserInfoDto;
import org.unicrm.auth.dto.UserRegDto;
import org.unicrm.auth.dto.UserVerificationDto;
import org.unicrm.auth.dto.kafka.KafkaUserDto;
import org.unicrm.auth.entities.Role;
import org.unicrm.auth.entities.Status;
import org.unicrm.auth.entities.User;
import org.unicrm.auth.exceptions.ResourceExistsException;
import org.unicrm.auth.exceptions.ResourceNotFoundException;
import org.unicrm.auth.mappers.EntityDtoMapper;
import org.unicrm.auth.repositories.UserRepository;
import org.unicrm.auth.validators.UpdatedUserValidator;
import org.unicrm.auth.validators.UserRegValidator;
import org.unicrm.auth.validators.UserVerificationValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final DepartmentService departmentService;
    private final RoleService roleService;
    private final SenderHandler senderHandler;
    private final PasswordEncoder passwordEncoder;
    private final UserRegValidator userRegValidator;
    private final UpdatedUserValidator updatedUserValidator;
    private final UserVerificationValidator userVerificationValidator;
    private final List<KafkaUserDto> listUserDtoForSend = new ArrayList<>();


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
    public List<KafkaUserDto> findAll() {
        return userRepository.findAll().stream().map(EntityDtoMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public KafkaUserDto getByUsername(String username) {
        return EntityDtoMapper.INSTANCE.toDto(findByUsername(username));
    }

    @Transactional
    public void saveNewUser(UserRegDto userRegDto) {
        userRegValidator.validate(userRegDto);
        User user = EntityDtoMapper.INSTANCE.toEntity(userRegDto);
        String[] username = userRegDto.getEmail().split("@");
        if (userRepository.existsUserByUsername(username[0])) throw new ResourceExistsException("This user already exists");
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
        updatedUserValidator.validate(updatedUserDto);
        User user = findByUsername(updatedUserDto.getUsername());
        if (user.getStatus() != Status.ACTIVE) throw new RuntimeException("Need to get verified");
        if (updatedUserDto.getEmail() != null) {
            user.setEmail(updatedUserDto.getEmail());
        }
        if (updatedUserDto.getFirstName() != null) user.setFirstName(updatedUserDto.getFirstName());
        if (updatedUserDto.getLastName() != null) user.setLastName(updatedUserDto.getLastName());
        if (updatedUserDto.getPassword() != null)
            user.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        listUserDtoForSend.add(EntityDtoMapper.INSTANCE.toDto(user));
        senderHandler.sendToKafka(listUserDtoForSend);
    }

    @Transactional
    public void changeLogin(String username, String login) {
        User user = findByUsername(username);
        if (login == null || login.isBlank()) throw new RuntimeException("login must not be empty");
        user.setUsername(login);
        listUserDtoForSend.add(EntityDtoMapper.INSTANCE.toDto(user));
        senderHandler.sendToKafka(listUserDtoForSend);
    }

    @Transactional
    public void userVerification(UserVerificationDto userVerificationDto) {
        userVerificationValidator.validate(userVerificationDto);
        User user = findByUsername(userVerificationDto.getUsername());
        try {
            user.setStatus(userVerificationDto.getStatus());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("incorrect status selected");
        }
        user.setDepartment(departmentService.findDepartmentByTitle(userVerificationDto.getDepartmentTitle()));
        listUserDtoForSend.add(EntityDtoMapper.INSTANCE.toDto(user));
        senderHandler.sendToKafka(listUserDtoForSend);
    }

    @Transactional
    public void addRole(String username, String roleName) {
        User user = findByUsername(username);
        user.getRoles().add(roleService.findRoleByName(roleName));
    }

    @Transactional(readOnly = true)
    public List<KafkaUserDto> findAllByStatusEqualsNoActive() {
        return userRepository.findAllByStatusEquals(Status.NOT_ACTIVE).stream().map(EntityDtoMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public UserInfoDto getUserInfo(String username) {
        User user = findByUsername(username);
        return EntityDtoMapper.INSTANCE.toInfoDto(user);
    }

    private User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException(String.format("User '%s' not found", username));
        }
        return user;
    }
}