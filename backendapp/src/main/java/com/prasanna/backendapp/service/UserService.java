package com.prasanna.backendapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prasanna.backendapp.entity.User;
import com.prasanna.backendapp.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 🔓 REGISTER (no encryption)
    public User register(User user) {
        return userRepository.save(user);
    }

    // 🔓 LOGIN (simple check)
    public Optional<User> login(String username, String password) {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent() && password.equals(user.get().getPassword())) {
            return user;
        }

        return Optional.empty();
    }
}