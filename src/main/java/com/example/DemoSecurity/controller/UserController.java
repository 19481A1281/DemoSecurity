package com.example.DemoSecurity.controller;

import com.example.DemoSecurity.model.User;
import com.example.DemoSecurity.repo.UserRepository;
import com.example.DemoSecurity.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        return userService.verify(user);
//        var u= userRepository.findByUserName(user.getUserName());
//        if(!Objects.isNull(u)) {
//            return "Success";
//        }
//       return "Failure";
    }
}
