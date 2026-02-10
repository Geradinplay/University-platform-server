package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // База сама будет создавать ID (1, 2, 3...)
    private Long id;

    @Column(nullable = false, unique = true) // Логин обязателен и не должен повторяться
    private String username;

    @Column(nullable = false) // ФИО пользователя
    private String name;

    @Column(nullable = false, unique = true) // Добавляем email, он был в твоем маппере
    private String email;

    @Column(nullable = false) // Пароль (хэш) обязателен для входа
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER; // Роль по умолчанию

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE; // Статус по умолчанию

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = true)
    private Professor professor; // Связь с профессором (может быть null)
}
