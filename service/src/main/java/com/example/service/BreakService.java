package com.example.service;

import com.example.dto.BreakDTO;
import com.example.entity.Break;
import com.example.entity.Lesson;
import com.example.entity.Schedule;
import com.example.exception.NotFoundException;
import com.example.mapper.BreakMapper;
import com.example.repository.BreakRepo;
import com.example.repository.LessonRepo;
import com.example.repository.ScheduleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BreakService {

    private final BreakRepo breakRepo;
    private final BreakMapper breakMapper;
    private final ScheduleRepo scheduleRepo;
    private final LessonRepo lessonRepo;

    @Transactional
    public Break createBreakForLesson(Lesson lesson, BreakDTO breakDto) {
        Break b = new Break();
        b.setLesson(lesson);
        b.setStartTime(breakDto.getStartTime());
        b.setEndTime(breakDto.getEndTime());
        b.setDay(lesson.getDay());
        if (breakDto.getScheduleId() != null) {
            b.setSchedule(scheduleRepo.findById(breakDto.getScheduleId())
                    .orElseThrow(() -> new NotFoundException("Schedule not found with id: " + breakDto.getScheduleId())));
        }
        return breakRepo.save(b);
    }

    @Transactional
    public void createBreak(Lesson lesson, LocalTime startTime, LocalTime endTime) {
        Break b = new Break();
        b.setLesson(lesson);
        b.setStartTime(startTime);
        b.setEndTime(endTime);
        b.setDay(lesson.getDay());
        breakRepo.save(b);
    }

    @Transactional
    public BreakDTO saveBreak(BreakDTO dto) {
        Break entity = breakMapper.toEntity(dto);
        if (dto.getScheduleId() != null) {
            entity.setSchedule(scheduleRepo.findById(dto.getScheduleId())
                    .orElseThrow(() -> new NotFoundException("Schedule not found with id: " + dto.getScheduleId())));
        }
        Break saved = breakRepo.save(entity);
        return breakMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<BreakDTO> getAllBreaks() {
        return breakRepo.findAll().stream().map(breakMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<BreakDTO> getBreaksByScheduleId(Long scheduleId) {
        // Проверяем существование расписания
        if (!scheduleRepo.existsById(scheduleId)) {
            throw new NotFoundException("Schedule not found with id: " + scheduleId);
        }
        return breakRepo.findByScheduleId(scheduleId)
                .stream()
                .map(breakMapper::toDto)
                .toList();
    }

    @Transactional
    public BreakDTO updateBreak(Long id, BreakDTO dto) {
        Break b = breakRepo.findById(id).orElseThrow(() -> new NotFoundException("Break not found: " + id));
        b.setStartTime(dto.getStartTime());
        b.setEndTime(dto.getEndTime());
        b.setDay(dto.getDay());
        if (dto.getScheduleId() != null) {
            b.setSchedule(scheduleRepo.findById(dto.getScheduleId())
                    .orElseThrow(() -> new NotFoundException("Schedule not found with id: " + dto.getScheduleId())));
        }
        Break saved = breakRepo.save(b);
        return breakMapper.toDto(saved);
    }

    @Transactional
    public void deleteBreak(Long id) {
        if (!breakRepo.existsById(id)) {
            throw new NotFoundException("Break not found with id: " + id);
        }
        breakRepo.deleteById(id);
    }
}