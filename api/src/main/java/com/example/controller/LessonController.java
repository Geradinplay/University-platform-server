package com.example.controller;

import com.example.dto.LessonDTO;
import com.example.service.LessonService;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    public ResponseEntity<LessonDTO> create(@Validated(LessonDTO.CreateGroup.class) @RequestBody LessonDTO dto) {
        return ResponseEntity.ok(lessonService.createLesson(dto));
    }

    @GetMapping
    public ResponseEntity<List<LessonDTO>> getLessons() {
        return ResponseEntity.ok(lessonService.getLessons());
    }

    @GetMapping("/schedule/{scheduleId}/lessons")
    public ResponseEntity<List<LessonDTO>> getLessonsBySchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(lessonService.getLessonsByScheduleId(scheduleId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDTO> update(@PathVariable Long id, @Validated(LessonDTO.UpdateGroup.class) @RequestBody LessonDTO dto) {
        return ResponseEntity.ok(lessonService.updateLesson(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}

