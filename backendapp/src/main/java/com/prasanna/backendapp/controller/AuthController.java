package com.prasanna.backendapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prasanna.backendapp.entity.User;
import com.prasanna.backendapp.service.UserService;
import com.prasanna.backendapp.util.JwtUtil;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Register API
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    // Login API


    @PostMapping("/login")
    public String login(@RequestBody User user) {

        Optional<User> loggedUser =
                userService.login(user.getUsername(), user.getPassword());

        if (loggedUser.isPresent()) {
            return JwtUtil.generateToken(user.getUsername()); // 🔥 TOKEN
        } else {
            return "Invalid Credentials";
        }
    }
}