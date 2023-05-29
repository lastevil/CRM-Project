package org.unicrm.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.auth.dto.RoleDto;
import org.unicrm.auth.entities.Role;
import org.unicrm.auth.exceptions.ResourceNotFoundException;
import org.unicrm.auth.mappers.EntityDtoMapper;
import org.unicrm.auth.repositories.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Role findRoleByName(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null || roleName.equals("ROLE_LOCAL_ADMIN")) {
            throw new ResourceNotFoundException(String.format("Role '%s' not found", roleName));
        }
        return role;
    }

    @Transactional(readOnly = true)
    public List<RoleDto> findAllRoles() {
        return roleRepository.findAllExceptNameIs("ROLE_LOCAL_ADMIN").stream().map(EntityDtoMapper.INSTANCE::toDto).collect(Collectors.toList());
    }
}
