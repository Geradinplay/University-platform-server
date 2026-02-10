package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username не может быть пустым")
    private String username;  // Может быть кириллица или латиница

    @NotBlank(message = "Имя не может быть пустым")
    private String name;      // ФИО пользователя (кириллица или латиница)

    @Email(message = "Email должен быть корректным")
    @NotBlank(message = "Email не может быть пустым")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;  // Может быть кириллица или латиница
}