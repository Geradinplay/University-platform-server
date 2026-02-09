package com.example.mapper;

import com.example.dto.ProfessorDTO;
import com.example.entity.Professor;
import org.springframework.stereotype.Component;

@Component
public class ProfessorMapper {

    public ProfessorDTO toDTO(Professor professor) {
        if (professor == null) return null;
        return new ProfessorDTO(
                professor.getId(),
                professor.getName()
        );
    }

    public Professor toEntity(ProfessorDTO dto) {
        if (dto == null) return null;
        Professor professor = new Professor();
        if (dto.getId() != null) {
            professor.setId(dto.getId());
        }
        professor.setName(dto.getName());
        return professor;
    }
}