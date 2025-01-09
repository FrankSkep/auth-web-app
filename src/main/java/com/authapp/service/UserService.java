package com.authapp.service;

import com.authapp.dto.RegisterDTO;
import com.authapp.entity.Role;
import com.authapp.entity.User;
import com.authapp.repository.RoleRepository;
import com.authapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(RegisterDTO registrationDto) {
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (userRepository.findByUsername(registrationDto.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }

        User user = User.builder()
                .username(registrationDto.getUsername())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .build();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        Role userRole = roleRepository.findByName("USER");

        if (userRole == null) {
            userRole = Role.builder()
                    .name("USER")
                    .build();
            roleRepository.save(userRole);
        }

        user.setRoles(Set.of(userRole));
        userRepository.save(user);
    }
}

