package com.example.controller;

import com.example.dto.UserDTO;
import com.example.entity.UserEntity;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepo;
import com.example.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepo userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    /**
     * GET /api/users - получить всех пользователей
     * Требуется JWT токен в заголовке Authorization
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestHeader("Authorization") String authHeader) {

        // Проверка токена
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.substring(7); // Убираем "Bearer "

        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(401).build();
        }

        // Получаем всех пользователей
        List<UserDTO> users = userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();

        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/users/search - поиск пользователя по username
     * Требуется JWT токен в заголовке Authorization
     */
    @GetMapping("/search")
    public ResponseEntity<UserDTO> getUserByUsername(
            @RequestParam String username,
            @RequestHeader("Authorization") String authHeader) {

        // Проверка токена
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(401).build();
        }

        // Получаем пользователя по username
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    /**
     * GET /api/users/{id} - получить пользователя по ID
     * Требуется JWT токен в заголовке Authorization
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        // Проверка токена
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(401).build();
        }

        // Получаем пользователя по ID
        UserEntity user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    /**
     * PUT /api/users/{id} - обновить пользователя
     * Требуется JWT токен в заголовке Authorization
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO updateDto,
            @RequestHeader("Authorization") String authHeader) {

        // Проверка токена
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(401).build();
        }

        // Получаем пользователя по ID
        UserEntity user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).build();
        }

        // Обновляем поля (только разрешенные)
        if (updateDto.getName() != null && !updateDto.getName().isEmpty()) {
            user.setName(updateDto.getName());
        }

        if (updateDto.getEmail() != null && !updateDto.getEmail().isEmpty()) {
            user.setEmail(updateDto.getEmail());
        }

        if (updateDto.getIsProfessor() != null) {
            user.setIsProfessor(updateDto.getIsProfessor());
        }

        // Сохраняем обновленного пользователя
        UserEntity updatedUser = userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    /**
     * DELETE /api/users/{id} - удалить пользователя
     * Требуется JWT токен в заголовке Authorization
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        // Проверка токена
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(401).build();
        }

        // Получаем пользователя по ID
        UserEntity user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).build();
        }

        // Удаляем пользователя
        userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}

