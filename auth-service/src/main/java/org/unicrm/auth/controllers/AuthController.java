package org.unicrm.auth.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.unicrm.auth.dto.JwtRequest;
import org.unicrm.auth.dto.JwtResponse;
import org.unicrm.auth.dto.UserRegDto;
import org.unicrm.auth.dto.UserVerificationDto;
import org.unicrm.auth.entities.Status;
import org.unicrm.auth.exceptions.AuthenticationException;
import org.unicrm.auth.services.UserService;
import org.unicrm.auth.utils.JwtTokenUtil;
import org.unicrm.lib.dto.UserDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/auth")
    public JwtResponse createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Incorrect username or password");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return new JwtResponse(token);
    }

    @PostMapping("/registration")
    public void saveNewUser(@RequestBody UserRegDto userRegDto) {
        userService.saveNewUser(userRegDto);
    }

    @GetMapping("/users/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.findAll();
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
}
