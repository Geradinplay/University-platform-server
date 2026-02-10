package com.example.service;

import com.example.entity.Schedule;
import com.example.exception.ScheduleNotFoundException;
import com.example.repository.ScheduleRepo;
import com.example.repository.LessonRepo;
import com.example.repository.BreakRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepo scheduleRepo;
    private final LessonRepo lessonRepo;
    private final BreakRepo breakRepo;

    public ScheduleService(ScheduleRepo scheduleRepo, LessonRepo lessonRepo, BreakRepo breakRepo) {
        this.scheduleRepo = scheduleRepo;
        this.lessonRepo = lessonRepo;
        this.breakRepo = breakRepo;
    }

    public List<Schedule> getAll() {
        return scheduleRepo.findAll();
    }

    @Transactional
    public Schedule create(String name, Long facultyId, Integer semester) {
        Schedule s = new Schedule();
        s.setName(name);
        s.setFacultyId(facultyId);
        s.setSemester(semester);
        return scheduleRepo.save(s);
    }

    public Schedule getById(Long id) {
        return scheduleRepo.findById(id).orElseThrow(() -> new ScheduleNotFoundException(id));
    }

    @Transactional
    public Schedule update(Long id, String name, Long facultyId, Integer semester) {
        Schedule s = scheduleRepo.findById(id).orElseThrow(() -> new ScheduleNotFoundException(id));
        s.setName(name);
        s.setFacultyId(facultyId);
        s.setSemester(semester);
        return scheduleRepo.save(s);
    }

    @Transactional
    public void delete(Long id) {
        if (!scheduleRepo.existsById(id)) {
            throw new ScheduleNotFoundException(id);
        }

        // Удаляем все перерывы для этого расписания
        breakRepo.deleteAll(breakRepo.findByScheduleId(id));

        // Удаляем все занятия для этого расписания
        lessonRepo.deleteAll(lessonRepo.findByScheduleId(id));

        // Удаляем само расписание
        scheduleRepo.deleteById(id);
    }

}
