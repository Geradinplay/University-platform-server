package com.example.controller;

import com.example.dto.BreakDTO;
import com.example.service.BreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/break")
@RequiredArgsConstructor
public class BreakController {

    private final BreakService breakService;

    // 1. Получить все перерывы (например, для проверки всех окон)
    @GetMapping
    public ResponseEntity<List<BreakDTO>> getAllBreaks() {
        return ResponseEntity.ok(breakService.getAllBreaks());
    }

    // 2. Добавление перерыва
    // Можно передавать lessonId в DTO или как RequestParam
    @PostMapping
    public ResponseEntity<BreakDTO> createBreak(@RequestBody BreakDTO breakDto) {
        BreakDTO created = breakService.saveBreak(breakDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // 3. Обновление перерыва
    @PutMapping("/{id}")
    public ResponseEntity<BreakDTO> updateBreak(@PathVariable Long id, @RequestBody BreakDTO breakDto) {
        BreakDTO updated = breakService.updateBreak(id, breakDto);
        return ResponseEntity.ok(updated);
    }

    // 4. Удаление перерыва
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBreak(@PathVariable Long id) {
        breakService.deleteBreak(id);
        return ResponseEntity.noContent().build();
    }
}