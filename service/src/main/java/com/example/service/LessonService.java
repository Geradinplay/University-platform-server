package com.example.service;

import com.example.dto.BreakDTO;
import com.example.dto.LessonDTO;
import com.example.entity.Break;
import com.example.entity.Lesson;
import com.example.exception.NotFoundException;
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
    private final ScheduleRepo scheduleRepo;

    private final LessonMapper lessonMapper;
    private final BreakMapper breakMapper;

    // ================= CREATE =================
    public LessonDTO createLesson(LessonDTO dto) {
        // 1. Мапим базовые поля (время, день)
        Lesson lesson = lessonMapper.toEntity(dto);

        // 2. Устанавливаем связи вручную, так как маппер не лезет в БД
        // Мы используем ID из DTO, чтобы найти сущности
        lesson.setSubject(subjectRepo.findById(dto.getSubjectId())
                .orElseThrow(() -> new NotFoundException("Subject not found with id: " + dto.getSubjectId())));

        lesson.setProfessor(professorRepo.findById(dto.getProfessorId())
                .orElseThrow(() -> new NotFoundException("Professor not found with id: " + dto.getProfessorId())));

        lesson.setClassroom(classroomRepo.findById(dto.getClassroomId())
                .orElseThrow(() -> new NotFoundException("Classroom not found with id: " + dto.getClassroomId())));

        lesson.setSchedule(scheduleRepo.findById(dto.getScheduleId())
                .orElseThrow(() -> new NotFoundException("Schedule not found with id: " + dto.getScheduleId())));

        // 3. Сохраняем готовую сущность
        Lesson saved = lessonRepo.save(lesson);
        return lessonMapper.toDTO(saved);
    }

    // ================= READ =================
    @Transactional(readOnly = true)
    public List<LessonDTO> getLessons() {
        return lessonRepo.findAll()
                .stream()
                .map(lessonMapper::toDTO)
                .toList();
    }

    // ================= UPDATE =================
    public LessonDTO updateLesson(Long id, LessonDTO dto) {
        Lesson lesson = lessonRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Lesson not found: " + id));

        // Обновляем базовые поля
        lesson.setStartTime(dto.getStartTime());
        lesson.setEndTime(dto.getEndTime());
        lesson.setDay(dto.getDay());

        // Обновляем связи, если в DTO пришли новые ID
        if (dto.getSubjectId() != null) {
            lesson.setSubject(subjectRepo.findById(dto.getSubjectId())
                    .orElseThrow(() -> new NotFoundException("Subject not found with id: " + dto.getSubjectId())));
        }
        if (dto.getProfessorId() != null) {
            lesson.setProfessor(professorRepo.findById(dto.getProfessorId())
                    .orElseThrow(() -> new NotFoundException("Professor not found with id: " + dto.getProfessorId())));
        }
        if (dto.getClassroomId() != null) {
            lesson.setClassroom(classroomRepo.findById(dto.getClassroomId())
                    .orElseThrow(() -> new NotFoundException("Classroom not found with id: " + dto.getClassroomId())));
        }
        if (dto.getScheduleId() != null) {
            lesson.setSchedule(scheduleRepo.findById(dto.getScheduleId())
                    .orElseThrow(() -> new NotFoundException("Schedule not found with id: " + dto.getScheduleId())));
        }

        Lesson saved = lessonRepo.save(lesson);
        return lessonMapper.toDTO(saved);
    }

    // ================= MIXED SCHEDULE =================
    @Transactional(readOnly = true)
    public List<LessonDTO> getLessonsByScheduleId(Long scheduleId) {
        // Проверяем существование расписания
        if (!scheduleRepo.existsById(scheduleId)) {
            throw new NotFoundException("Schedule not found with id: " + scheduleId);
        }
        return lessonRepo.findByScheduleId(scheduleId)
                .stream()
                .map(lessonMapper::toDTO)
                .toList();
    }

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
            throw new NotFoundException("Lesson not found with id: " + id);
        }
        lessonRepo.deleteById(id);
    }
}