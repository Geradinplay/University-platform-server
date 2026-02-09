package com.example.service;

import com.example.dto.BreakDTO;
import com.example.entity.Break;
import com.example.entity.Lesson;
import com.example.mapper.BreakMapper;
import com.example.repository.BreakRepo;
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

    @Transactional
    public Break createBreakForLesson(Lesson lesson, BreakDTO breakDto) {
        if (breakDto == null) return null;

        // Валидация: время конца не может быть раньше начала
        if (breakDto.getEndTime().isBefore(breakDto.getStartTime())) {
            throw new IllegalArgumentException("Время окончания перерыва не может быть раньше времени начала");
        }

        Break breakEntity = new Break();
        breakEntity.setStartTime(breakDto.getStartTime());
        breakEntity.setEndTime(breakDto.getEndTime());
        breakEntity.setDay(lesson.getDay()); // Логично брать день из урока
        breakEntity.setLesson(lesson);

        return breakRepo.save(breakEntity);
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
        // Маппим DTO в сущность
        Break entity = breakMapper.toEntity(dto);

        // Если перерыв должен быть привязан к уроку,
        // здесь нужно найти урок в репо и сделать setLesson()

        Break saved = breakRepo.save(entity);
        return breakMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<BreakDTO> getAllBreaks() {
        return breakRepo.findAll().stream()
                .map(breakMapper::toDto)
                .toList();
    }

    @Transactional
    public BreakDTO updateBreak(Long id, BreakDTO dto) {
        Break breakEntity = breakRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Break not found"));

        // Валидация: время конца не может быть раньше начала
        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new IllegalArgumentException("Время окончания перерыва не может быть раньше времени начала");
        }

        // Обновляем поля
        breakEntity.setStartTime(dto.getStartTime());
        breakEntity.setEndTime(dto.getEndTime());
        breakEntity.setDay(dto.getDay());

        Break updated = breakRepo.save(breakEntity);
        return breakMapper.toDto(updated);
    }

    @Transactional
    public void deleteBreak(Long id) {
        if (!breakRepo.existsById(id)) {
            throw new RuntimeException("Break not found");
        }
        breakRepo.deleteById(id);
    }
}