package com.example.mapper;

import com.example.dto.FacultyDTO;
import com.example.entity.Faculties;
import org.springframework.stereotype.Component;

@Component
public class FacultyMapper {

    // Из Entity в DTO (для ответов API)
    public FacultyDTO toDTO(Faculties entity) {
        if (entity == null) return null;
        return new FacultyDTO(
                entity.getId(),
                entity.getName(),
                entity.getShort_name()
        );
    }

    // Из DTO в Entity (для сохранения в БД)
    public Faculties toEntity(FacultyDTO dto) {
        if (dto == null) return null;
        Faculties entity = new Faculties();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setShort_name(dto.getShortName());
        return entity;
    }
}