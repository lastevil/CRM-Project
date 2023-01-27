package org.unicrm.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.auth.dto.UserDto;
import org.unicrm.auth.entities.Role;
import org.unicrm.auth.entities.Status;
import org.unicrm.auth.entities.User;
import org.unicrm.auth.exceptions.ResourceNotFoundException;
import org.unicrm.auth.mappers.EntityDtoMapper;
import org.unicrm.auth.mappers.UserConverter;
import org.unicrm.auth.repositories.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException(String.format("User '%s' not found", username));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id:'%d' not found", id)));
        return userConverter.entityToDto(user);
//        return EntityDtoMapper.INSTANCE.toDto(user);
    }

    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException(String.format("User '%s' not found", username));
        }
        return userConverter.entityToDto(user);
//        return EntityDtoMapper.INSTANCE.toDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(userConverter::entityToDto).collect(Collectors.toList());
//        return userRepository.findAll().stream().map(EntityDtoMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void changeStatus(String username, String status) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException(String.format("User '%s' not found", username));
        }
        user.setStatus(Status.valueOf(status));
    }
}
