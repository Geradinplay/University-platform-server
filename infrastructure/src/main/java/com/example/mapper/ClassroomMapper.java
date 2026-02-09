package com.example.mapper;

import com.example.dto.ClassroomDTO;
import com.example.entity.Classroom;
import org.springframework.stereotype.Component;

@Component
public class ClassroomMapper {

    public ClassroomDTO toDTO(Classroom classroom) {
        if (classroom == null) return null;
        return new ClassroomDTO(
                classroom.getId(),
                classroom.getNumber()
        );
    }

    public Classroom toEntity(ClassroomDTO dto) {
        if (dto == null) return null;
        Classroom classroom = new Classroom();
        if (dto.getId() != null) {
            classroom.setId(dto.getId());
        }
        classroom.setNumber(dto.getNumber());
        return classroom;
    }
}