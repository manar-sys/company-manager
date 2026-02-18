package com.example.demo.component;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        // Create ADMIN if not exists
        if (userRepository.findByUsername("admin").isEmpty()) {

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("1234"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            System.out.println("Admin user created!");
        }

        // Create USER if not exists
        if (userRepository.findByUsername("user").isEmpty()) {

            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("1234"))
                    .role(Role.USER)
                    .build();

            userRepository.save(user);
            System.out.println("Normal user created!");
        }
    }
}