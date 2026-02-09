package com.example.mapper;

import com.example.dto.ClassroomDTO;
import com.example.dto.LessonDTO;
import com.example.dto.ProfessorDTO;
import com.example.dto.SubjectDTO;
import com.example.entity.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {

    // Убрали финальное поле BreakMapper и @RequiredArgsConstructor, так как зависимости больше нет

    public LessonDTO toDTO(Lesson lesson) {
        if (lesson == null) return null;

        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());

        // Время и день
        dto.setStartTime(lesson.getStartTime());
        dto.setEndTime(lesson.getEndTime());
        dto.setDay(lesson.getDay());

        // Маппинг связей (объектов для чтения)
        if (lesson.getSubject() != null) {
            dto.setSubject(new SubjectDTO(
                    lesson.getSubject().getId(),
                    lesson.getSubject().getName()
            ));
        }

        if (lesson.getProfessor() != null) {
            dto.setProfessor(new ProfessorDTO(
                    lesson.getProfessor().getId(),
                    lesson.getProfessor().getName()
            ));
        }

        if (lesson.getClassroom() != null) {
            dto.setClassroom(new ClassroomDTO(
                    lesson.getClassroom().getId(),
                    lesson.getClassroom().getNumber()
            ));
        }

        // Блок с breakInfo удален, так как таблицы не связаны

        return dto;
    }

    public Lesson toEntity(LessonDTO dto) {
        if (dto == null) return null;

        Lesson lesson = new Lesson();
        if (dto.getId() != null) {
            lesson.setId(dto.getId());
        }

        lesson.setStartTime(dto.getStartTime());
        lesson.setEndTime(dto.getEndTime());
        lesson.setDay(dto.getDay());

        // ID связей (subjectId, professorId и т.д.) обрабатываются в Service через репозитории
        return lesson;
    }

    /**
     * Полезный метод для обновления существующей сущности (используется в UPDATE)
     */
    public void updateEntityFromDto(LessonDTO dto, Lesson lesson) {
        if (dto == null) return;

        lesson.setStartTime(dto.getStartTime());
        lesson.setEndTime(dto.getEndTime());
        lesson.setDay(dto.getDay());
    }
}