package org.unicrm.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unicrm.chat.mapper.UserRegistration;
import org.unicrm.chat.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @PostMapping("/registration")
    public void registrationUser(@RequestBody UserRegistration user) {
        userService.save(user);
    }


}
