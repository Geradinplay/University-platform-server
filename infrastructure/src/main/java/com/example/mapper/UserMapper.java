package com.example.mapper;

import com.example.dto.UserDTO;
import com.example.entity.UserEntity;
import com.example.entity.UserRole;
import com.example.entity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDto(UserEntity entity) {
        if (entity == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());

        dto.setRole(entity.getRole().name());

        dto.setIsBanned(entity.getStatus() == UserStatus.BANNED);

        // Маппим профессора
        dto.setProfessorId(entity.getProfessor() != null ? entity.getProfessor().getId() : null);

        return dto;
    }

    public UserEntity toEntity(UserDTO dto) {
        if (dto == null) return null;

        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());

        // Маппим обратно в Enum
        if (dto.getRole() != null) {
            entity.setRole(UserRole.valueOf(dto.getRole()));
        }

        // Логика бана при сохранении (если нужно)
        if (dto.getIsBanned()) {
            entity.setStatus(UserStatus.BANNED);
        } else {
            entity.setStatus(UserStatus.ACTIVE);
        }

        // Профессор маппится отдельно в сервисе (так как нужно получить из БД по ID)
        // Здесь мы не маппим professor, это должно быть в сервисе

        return entity;
    }
}