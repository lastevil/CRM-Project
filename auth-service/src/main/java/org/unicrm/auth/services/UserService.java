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
import org.unicrm.auth.dto.UserRolesDto;
import org.unicrm.auth.dto.kafka.KafkaUserDto;
import org.unicrm.auth.entities.Department;
import org.unicrm.auth.entities.Role;
import org.unicrm.auth.entities.Status;
import org.unicrm.auth.entities.User;
import org.unicrm.auth.exceptions.NotActiveAccountException;
import org.unicrm.auth.exceptions.ResourceExistsException;
import org.unicrm.auth.exceptions.ResourceNotFoundException;
import org.unicrm.auth.mappers.EntityDtoMapper;
import org.unicrm.auth.repositories.UserRepository;
import org.unicrm.auth.validators.UpdatedUserValidator;
import org.unicrm.auth.validators.UserRegValidator;
import org.unicrm.auth.validators.UserVerificationValidator;

import javax.validation.ValidationException;
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
    private final SenderHandler senderHandler;
    private final PasswordEncoder passwordEncoder;
    private final UserRegValidator userRegValidator;
    private final UpdatedUserValidator updatedUserValidator;
    private final UserVerificationValidator userVerificationValidator;
    private final List<KafkaUserDto> listUserDtoForSend = new ArrayList<>();

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = login.contains("@") ? findUserByEmail(login) : findUserByUsername(login);
        if (user.getStatus() == Status.NOT_ACTIVE) throw new NotActiveAccountException("Account is not active");
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserInfoDto> findAll() {
        return userRepository.findAllExceptLocalAdmin("Admin").stream().map(EntityDtoMapper.INSTANCE::toInfoDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserInfoDto getByUsername(String username) {
        return EntityDtoMapper.INSTANCE.toInfoDto(findUserByUsername(username));
    }

    @Transactional
    public void saveNewUser(UserRegDto userRegDto) {
        userRegValidator.validate(userRegDto);
        User user = EntityDtoMapper.INSTANCE.toEntity(userRegDto);
        if (Boolean.TRUE.equals(userRepository.existsUserByUsername(userRegDto.getLogin()))) throw new ResourceExistsException("User with this login already exists");
        if (Boolean.TRUE.equals(userRepository.existsUserByEmail(userRegDto.getEmail()))) throw new ResourceExistsException("User with this email already exists");
        user.setUsername(userRegDto.getLogin());
        user.setPassword(passwordEncoder.encode(userRegDto.getPassword()));
        Role roleUser = roleService.findRoleByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);
        user.setRoles(userRoles);
        user.setStatus(Status.NEW);
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(UpdatedUserDto updatedUserDto) {
        updatedUserValidator.validate(updatedUserDto);
        applyChangesForUser(updatedUserDto);
        sendUser(updatedUserDto.getUsername());
    }

    @Transactional
    public void changeLogin(UUID userId, String login) {
        applyChangeLogin(userId, login);
        sendUser(login);
    }

    @Transactional
    public void applyChangesForUser(UpdatedUserDto updatedUserDto) {
        User user = findUserByUsername(updatedUserDto.getUsername());
        if (user.getStatus() != Status.ACTIVE) throw new ValidationException("Need to get verified");
        if (updatedUserDto.getEmail() != null) {
            user.setEmail(updatedUserDto.getEmail());
        }
        if (updatedUserDto.getFirstName() != null) user.setFirstName(updatedUserDto.getFirstName());
        if (updatedUserDto.getLastName() != null) user.setLastName(updatedUserDto.getLastName());
        if (updatedUserDto.getPassword() != null)
            user.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void activateUser(UUID uuid) {
        findUserById(uuid).setStatus(Status.ACTIVE);
    }
    @Transactional
    public void deactivateUser(UUID uuid) {
        findUserById(uuid).setStatus(Status.NOT_ACTIVE);
    }

    @Transactional
    public void applyChangeLogin(UUID userId, String login) {
        User user = findUserById(userId);
        if (login == null || login.isBlank()) throw new ValidationException("login must not be empty");
        user.setUsername(login);
        userRepository.save(user);
    }

    @Transactional
    public void addRole(UUID userId, String roleName) {
        User user = findUserById(userId);
        user.getRoles().add(roleService.findRoleByName(roleName));
    }

    @Transactional
    public void deleteRole(UUID userId, String roleName) {
        User user = findUserById(userId);
        if (!user.getRoles().remove(roleService.findRoleByName(roleName))) throw new ResourceNotFoundException("The user does not have these rights");
    }

    @Transactional(readOnly = true)
    public List<UserInfoDto> findAllByStatusEqualsNoActive() {
        return userRepository.findAllByStatusEquals(Status.NOT_ACTIVE).stream().map(EntityDtoMapper.INSTANCE::toInfoDto).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<UserInfoDto> findAllByStatusEqualsActive() {
        return userRepository.findAllByStatusEqualsAndUsernameIsNot(Status.ACTIVE, "Admin").stream().map(EntityDtoMapper.INSTANCE::toInfoDto).collect(Collectors.toList());
    }

    private User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException(String.format("User '%s' not found", username));
        }
        return user;
    }
    private User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException(String.format("User with email: '%s' not found", email));
        }
        return user;
    }
    private User findUserById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id:'%s' not found", uuid)));
    }

    private void sendUser(String username) {
        User user = findUserByUsername(username);
        listUserDtoForSend.add(EntityDtoMapper.INSTANCE.toDto(user));
        senderHandler.sendToKafka(listUserDtoForSend);
    }

    @Transactional
    public void changeDepartment(UUID userUuid, Long departmentId) {
        User user = findUserById(userUuid);
        if (user.getStatus() != Status.ACTIVE) activateUser(userUuid);
        user.setDepartment(departmentService.findDepartmentById(departmentId));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserRolesDto> findAllByDepartment(Long departmentId) {
        List<User> users = findAllByDepartment(departmentService.findDepartmentById(departmentId));
        return users.stream().map(EntityDtoMapper.INSTANCE::toUserRolesDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<User> findAllByDepartment(Department department) {
        return userRepository.findAllByDepartment(department);
    }

}