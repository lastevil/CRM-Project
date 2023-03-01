package org.unicrm.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unicrm.chat.dto.LocalUserDto;
import org.unicrm.chat.model.UserRegistration;
import org.unicrm.chat.service.UserService;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public List<LocalUserDto> findAll() {
        return userService.findAllUsers();
    }

}
