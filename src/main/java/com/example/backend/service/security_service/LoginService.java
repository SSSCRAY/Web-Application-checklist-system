package com.example.backend.service.security_service;

import com.example.backend.entity.User;
import com.example.backend.reposiroty.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean LoginCheck(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return false;
        }

        return passwordEncoder.matches(password, user.getPassword());

    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
