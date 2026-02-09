package com.example.service;

import com.example.dto.LessonDTO;
import com.example.entity.Lesson;
import com.example.repository.ScheduleRepo;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated // Enables Spring Validation for this service
public class ScheduleService {
    private final ScheduleRepo scheduleRepo;

    public ScheduleService(ScheduleRepo scheduleRepo) {
        this.scheduleRepo = scheduleRepo;
    }

    // Adding a filter parameter with validation
    List<LessonDTO> getSchedule(@NotBlank(message = "Filter cannot be blank") String filter) {
        // Validate the filter parameter
        List<Lesson> schedule = scheduleRepo.findAllWithBreaks();
        if (schedule.isEmpty()) {
            // Handle empty schedule case
        }

        // Convert Lesson entities to LessonDTOs
        return schedule.stream()
                .map(lesson -> {
                    LessonDTO dto = new LessonDTO();
                    dto.setId(lesson.getId());
                    dto.setName(lesson.getName());
                    dto.setDescription(lesson.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
