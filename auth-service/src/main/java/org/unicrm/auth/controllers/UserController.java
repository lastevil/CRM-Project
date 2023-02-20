package org.unicrm.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unicrm.auth.dto.UpdatedUserDto;
import org.unicrm.auth.dto.UserInfoDto;
import org.unicrm.auth.dto.UserVerificationDto;
import org.unicrm.auth.services.UserService;
import org.unicrm.lib.dto.UserDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
@Tag(name = "UserController", description = "All possible operations with users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user by username")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }
    @Operation(summary = "Getting a list of all users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.findAll();
    }

    @Operation(summary = "Request to change user data")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/users/update")
    public void updateUser(@RequestBody UpdatedUserDto updatedUserDto) {
        userService.updateUser(updatedUserDto);
    }

    @Operation(summary = "Request to add rights for users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users/roles/{username}/{roleName}")
    public void addRoleToUser(@PathVariable String username, @PathVariable String roleName) {
        userService.addRole(username, roleName);
    }

    @Operation(summary = "Checking, setting the status and department of the user. Sending users to other services")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("users/verification")
    public void userVerification(@RequestBody UserVerificationDto userVerificationDto){
        userService.userVerification(userVerificationDto.getUsername(), userVerificationDto.getStatus(), userVerificationDto.getDepartmentTitle());
    }

    @Operation(summary = "Request to get all inactive users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users/not_active")
    public List<UserDto> findAllByStatusEqualsNoActive() {
        return userService.findAllByStatusEqualsNoActive();
    }

    @Operation(summary = "Request to change login by administrator")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users/{username}/change/{login}")
    public void changeLogin(@PathVariable String username, @PathVariable String login) {
        userService.changeLogin(username, login);
    }

    @Operation(summary = "User information request")
    @GetMapping("/users/{username}/info")
    public UserInfoDto getUserInfo(@PathVariable String username) {
        return userService.getUserInfo(username);
    }
}
