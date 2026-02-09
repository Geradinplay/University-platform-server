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

    private final LessonService scheduleService;

    public LessonController(LessonService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<LessonDTO> create(@Validated(LessonDTO.CreateGroup.class) @RequestBody LessonDTO dto) {
        return ResponseEntity.ok(scheduleService.createLesson(dto));
    }

    @GetMapping
    public ResponseEntity<List<LessonDTO>> getSchedule() {
        return ResponseEntity.ok(scheduleService.getSchedule());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDTO> update(@PathVariable Long id, @Validated(LessonDTO.UpdateGroup.class) @RequestBody LessonDTO dto) {
        return ResponseEntity.ok(scheduleService.updateLesson(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        scheduleService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}
