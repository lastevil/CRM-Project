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
import org.unicrm.auth.dto.kafka.KafkaUserDto;
import org.unicrm.auth.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
@Tag(name = "UserController", description = "All possible operations with users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user by username")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LOCAL_ADMIN')")
    @GetMapping("/users/{username}")
    public UserInfoDto getUserByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @Operation(summary = "Getting a list of all users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LOCAL_ADMIN')")
    @GetMapping("/users")
    public List<UserInfoDto> getAllUsers() {
        return userService.findAll();
    }

    @Operation(summary = "Request to change user data")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/users/update")
    public void updateUser(@RequestBody UpdatedUserDto updatedUserDto) {
        userService.updateUser(updatedUserDto);
    }

    @Operation(summary = "Request to add rights for users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LOCAL_ADMIN')")
    @GetMapping("/users/roles/{username}/{roleName}")
    public void addRoleToUser(@PathVariable String username, @PathVariable String roleName) {
        userService.addRole(username, roleName);
    }

    @Operation(summary = "Checking, setting the status and department of the user. Sending users to other services")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LOCAL_ADMIN')")
    @PostMapping("users/verification")
    public void userVerification(@RequestBody UserVerificationDto userVerificationDto){
        userService.userVerification(userVerificationDto);
    }

    @Operation(summary = "Request to get all inactive users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LOCAL_ADMIN')")
    @GetMapping("/users/not_active")
    public List<UserInfoDto> findAllByStatusEqualsNoActive() {
        return userService.findAllByStatusEqualsNoActive();
    }

    @Operation(summary = "Request to get all active users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LOCAL_ADMIN')")
    @GetMapping("/users/active")
    public List<UserInfoDto> findAllByStatusEqualsActive() {
        return userService.findAllByStatusEqualsActive();
    }

    @Operation(summary = "Request to change login by administrator")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LOCAL_ADMIN')")
    @GetMapping("/users/{username}/change/{login}")
    public void changeLogin(@PathVariable String username, @PathVariable String login) {
        userService.changeLogin(username, login);
    }

    @Operation(summary = "User information request")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users/{username}/info")
    public UserInfoDto getUserInfo(@PathVariable String username) {
        return userService.getUserInfo(username);
    }

    @Operation(summary = "User activation")
    @PostMapping("/users/activate")
    public void activateUser(@RequestParam UUID userUuid) {
        userService.activateUser(userUuid);
    }

    @Operation(summary = "User deactivation")
    @PostMapping("/users/deactivate")
    public void deactivateUser(@RequestParam UUID userUuid) {
        userService.deactivateUser(userUuid);
    }
}
