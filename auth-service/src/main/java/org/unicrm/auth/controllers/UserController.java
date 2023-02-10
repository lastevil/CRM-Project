package org.unicrm.auth.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unicrm.auth.dto.UpdatedUserDto;
import org.unicrm.auth.dto.UserVerificationDto;
import org.unicrm.auth.entities.Status;
import org.unicrm.auth.services.UserService;
import org.unicrm.lib.dto.UserDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.findAll();
    }

    @PreAuthorize("authenticated()")
    @PostMapping("/users/update")
    public void updateUser(@RequestBody UpdatedUserDto updatedUserDto) {
        userService.updateUser(updatedUserDto);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users/status/{username}/{status}")
    public void changeStatus(@PathVariable String username, @PathVariable Status status) {
        userService.changeStatus(username, status);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users/departments/{username}/{departmentTitle}")
    public void assignDepartment(@PathVariable String username, @PathVariable String departmentTitle) {
        userService.assignDepartment(username, departmentTitle);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users/roles/{username}/{roleName}")
    public void addRoleToUser(@PathVariable String username, @PathVariable String roleName) {
        userService.addRole(username, roleName);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("users/verification")
    public void userVerification(@RequestBody UserVerificationDto userVerificationDto){
        userService.userVerification(userVerificationDto.getUsername(), userVerificationDto.getStatus(), userVerificationDto.getDepartmentTitle());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users/not_active")
    public List<UserDto> findAllByStatusEqualsNoActive() {
        return userService.findAllByStatusEqualsNoActive();
    }
}
