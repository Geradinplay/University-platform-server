package com.example.service;

import com.example.dto.BreakDTO;
import com.example.dto.LessonDTO;
import com.example.entity.Break;
import com.example.entity.Lesson;
import com.example.mapper.BreakMapper;
import com.example.mapper.LessonMapper;
import com.example.repository.*; // Импортируем все нужные репозитории
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonService {

    private final LessonRepo lessonRepo;
    private final BreakRepo breakRepo;

    // Добавляем недостающие репозитории для поиска связей
    private final SubjectRepo subjectRepo;
    private final ProfessorRepo professorRepo;
    private final ClassroomRepo classroomRepo;

    private final LessonMapper lessonMapper;
    private final BreakMapper breakMapper;

    // ================= CREATE =================
    public LessonDTO createLesson(LessonDTO dto) {
        // 1. Мапим базовые поля (время, день)
        Lesson lesson = lessonMapper.toEntity(dto);

        // 2. Устанавливаем связи вручную, так как маппер не лезет в БД
        // Мы используем ID из DTO, чтобы найти сущности
        lesson.setSubject(subjectRepo.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + dto.getSubjectId())));

        lesson.setProfessor(professorRepo.findById(dto.getProfessorId())
                .orElseThrow(() -> new RuntimeException("Professor not found with id: " + dto.getProfessorId())));

        lesson.setClassroom(classroomRepo.findById(dto.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + dto.getClassroomId())));

        // 3. Сохраняем готовую сущность
        Lesson saved = lessonRepo.save(lesson);
        return lessonMapper.toDTO(saved);
    }

    // ================= READ =================
    @Transactional(readOnly = true)
    public List<LessonDTO> getSchedule() {
        return lessonRepo.findAll()
                .stream()
                .map(lessonMapper::toDTO)
                .toList();
    }

    // ================= UPDATE =================
    public LessonDTO updateLesson(Long id, LessonDTO dto) {
        Lesson existingLesson = lessonRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + id));

        // Обновляем базовые поля
        existingLesson.setStartTime(dto.getStartTime());
        existingLesson.setEndTime(dto.getEndTime());
        existingLesson.setDay(dto.getDay());

        // Обновляем связи, если в DTO пришли новые ID
        if (dto.getSubjectId() != null) {
            existingLesson.setSubject(subjectRepo.findById(dto.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Subject not found")));
        }
        if (dto.getProfessorId() != null) {
            existingLesson.setProfessor(professorRepo.findById(dto.getProfessorId())
                    .orElseThrow(() -> new RuntimeException("Professor not found")));
        }
        if (dto.getClassroomId() != null) {
            existingLesson.setClassroom(classroomRepo.findById(dto.getClassroomId())
                    .orElseThrow(() -> new RuntimeException("Classroom not found")));
        }

        Lesson updated = lessonRepo.save(existingLesson);
        return lessonMapper.toDTO(updated);
    }

    // ================= MIXED SCHEDULE =================
    @Transactional(readOnly = true)
    public List<Object> getFullSchedule(Integer day) {
        List<Lesson> lessons = lessonRepo.findByDay(day);
        List<Break> breaks = breakRepo.findByDay(day);

        List<Object> combined = new ArrayList<>();

        combined.addAll(lessons.stream().map(lessonMapper::toDTO).toList());
        combined.addAll(breaks.stream().map(breakMapper::toDto).toList());

        combined.sort(Comparator.comparing(item -> {
            if (item instanceof LessonDTO) return ((LessonDTO) item).getStartTime();
            if (item instanceof BreakDTO) return ((BreakDTO) item).getStartTime();
            return java.time.LocalTime.MAX;
        }));

        return combined;
    }

    // ================= DELETE =================
    public void deleteLesson(Long id) {
        if (!lessonRepo.existsById(id)) {
            throw new RuntimeException("Lesson not found with id: " + id);
        }
        lessonRepo.deleteById(id);
    }
}