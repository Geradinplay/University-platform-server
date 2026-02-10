package com.example.dto;

import com.example.entity.UserRole;
import com.example.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String name; // ФИО пользователя
    private String email;
    private String role;
    private Boolean isBanned;
    private Long professorId; // ID профессора (может быть null)
}