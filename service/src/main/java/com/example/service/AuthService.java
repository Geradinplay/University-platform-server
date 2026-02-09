package com.example.service;

import com.example.dto.RegisterRequest;
import com.example.entity.UserEntity;
import com.example.entity.UserRole;
import com.example.entity.UserStatus;
import com.example.exception.AuthException;
import com.example.exception.UserAlreadyExistsException;
import com.example.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity register(RegisterRequest dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException("Username '" + dto.getUsername() + "' is already taken");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Email '" + dto.getEmail() + "' is already in use");
        }

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.ACTIVE);

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // Обработка race condition: если два запроса одновременно создают пользователя
            String message = e.getMessage();
            if (message != null && message.toLowerCase().contains("username")) {
                throw new UserAlreadyExistsException("Username '" + dto.getUsername() + "' is already taken");
            } else if (message != null && message.toLowerCase().contains("email")) {
                throw new UserAlreadyExistsException("Email '" + dto.getEmail() + "' is already in use");
            }
            throw new UserAlreadyExistsException("User registration failed due to duplicate data");
        }
    }

    public UserEntity authenticate(String username, String password) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException("Invalid username or password");
        }

        if (user.getStatus() == UserStatus.BANNED) {
            throw new AuthException("Account is blocked");
        }

        return user;
    }
}