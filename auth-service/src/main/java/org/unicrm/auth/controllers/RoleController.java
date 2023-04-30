package org.unicrm.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unicrm.auth.dto.RoleDto;
import org.unicrm.auth.services.RoleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
@Tag(name = "RoleController", description = "All possible operations with roles")
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Getting a list of all roles")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LOCAL_ADMIN')")
    @GetMapping("/roles")
    public List<RoleDto> getAllUsers() {
        return roleService.findAllRoles();
    }

}
