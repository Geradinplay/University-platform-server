package com.example.controller;

import com.example.dto.ScheduleDTO;
import com.example.dto.LessonDTO;
import com.example.dto.BreakDTO;
import com.example.entity.Schedule;
import com.example.service.ScheduleService;
import com.example.service.LessonService;
import com.example.service.BreakService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final LessonService lessonService;
    private final BreakService breakService;

    public ScheduleController(ScheduleService scheduleService, LessonService lessonService, BreakService breakService) {
        this.scheduleService = scheduleService;
        this.lessonService = lessonService;
        this.breakService = breakService;
    }

    @GetMapping
    public List<ScheduleDTO> getAll() {
        return scheduleService.getAll().stream()
                .map(s -> new ScheduleDTO(s.getId(), s.getName(), s.getFacultyId(), s.getSemester()))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleDTO create(@RequestBody @Valid ScheduleDTO dto) {
        Schedule created = scheduleService.create(dto.getName(), dto.getFacultyId(), dto.getSemester());
        return new ScheduleDTO(created.getId(), created.getName(), created.getFacultyId(), created.getSemester());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getById(@PathVariable Long id) {
        Schedule s = scheduleService.getById(id);
        return ResponseEntity.ok(new ScheduleDTO(s.getId(), s.getName(), s.getFacultyId(), s.getSemester()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> update(@PathVariable Long id, @RequestBody @Valid ScheduleDTO dto) {
        Schedule updated = scheduleService.update(id, dto.getName(), dto.getFacultyId(), dto.getSemester());
        return ResponseEntity.ok(new ScheduleDTO(updated.getId(), updated.getName(), updated.getFacultyId(), updated.getSemester()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        scheduleService.delete(id);
    }

    @GetMapping("/{id}/lessons")
    public ResponseEntity<List<LessonDTO>> getLessonsBySchedule(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLessonsByScheduleId(id));
    }

    @GetMapping("/{id}/breaks")
    public ResponseEntity<List<BreakDTO>> getBreaksBySchedule(@PathVariable Long id) {
        return ResponseEntity.ok(breakService.getBreaksByScheduleId(id));
    }
}
