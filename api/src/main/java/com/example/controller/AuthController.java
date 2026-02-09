package com.example.controller;

import com.example.dto.RegisterRequest;
import com.example.dto.UserDTO;
import com.example.entity.UserEntity;
import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import com.example.mapper.UserMapper;
import com.example.service.AuthService;
import com.example.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest request) {
        // Если регистрация не пройдет (логин занят), GlobalExceptionHandler
        // сам отправит 400 Bad Request с английским сообщением.
        UserEntity user = authService.register(request);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Проверка пароля теперь внутри authService.authenticate
        UserEntity user = authService.authenticate(request.getUsername(), request.getPassword());

        // Генерируем токен
        String token = jwtService.generateToken(user);

        // Возвращаем объект вместо голой строки
        return ResponseEntity.ok(new AuthResponse(token));
    }
}