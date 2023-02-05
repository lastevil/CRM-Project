package org.unicrm.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.auth.entities.Role;
import org.unicrm.auth.exceptions.ResourceNotFoundException;
import org.unicrm.auth.repositories.RoleRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Role findRoleByName(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new ResourceNotFoundException(String.format("Role '%s' not found", roleName));
        }
        return role;
    }
}
