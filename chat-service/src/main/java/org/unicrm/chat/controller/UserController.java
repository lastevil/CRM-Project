package org.unicrm.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unicrm.chat.dto.UserDto;
import org.unicrm.chat.model.UserRegistration;
import org.unicrm.chat.service.UserService;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @PostMapping("/registration")
    public void registrationUser(@RequestBody UserRegistration user) {
        userService.save(user);
    }

    @GetMapping("/users")
    public List<UserDto> findAll() {
        return userService.findAllUsers();
    }

}
