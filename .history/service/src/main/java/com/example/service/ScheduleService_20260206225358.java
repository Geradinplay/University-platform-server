package com.example.service;

import com.example.entity.Lesson;
import com.example.repository.ScheduleRepo;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Service
@Validated // Enables Spring Validation for this service
public class ScheduleService {
    private final ScheduleRepo scheduleRepo;

    public ScheduleService(ScheduleRepo scheduleRepo) {
        this.scheduleRepo = scheduleRepo;
    }

    // Adding a filter parameter with validation
    List<Lesson> getSchedule(@NotBlank(message = "Filter cannot be blank") String filter) {
        // Validate the filter parameter
        List<Lesson> schedule = scheduleRepo.findAllWithBreaks();
        if (schedule.isEmpty()) {
            // Handle empty schedule case
        }

        return scheduleRepo.findAllWithBreaks();
    }
}
