package com.example.controller;

import com.example.dto.CreateClassroomDTO;
import com.example.dto.ClassroomDTO;
import com.example.entity.Classroom;
import com.example.repository.ClassroomRepo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/classrooms")
public class RoomController {

    private final ClassroomRepo classroomRepo;

    public RoomController(ClassroomRepo classroomRepo) {
        this.classroomRepo = classroomRepo;
    }

    @GetMapping
    public List<ClassroomDTO> getAll() {
        return classroomRepo.findAll().stream()
                .map(c -> new ClassroomDTO(c.getId(), c.getNumber()))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClassroomDTO create(@RequestBody @Valid CreateClassroomDTO dto) {
        Classroom classroom = new Classroom();
        classroom.setNumber(dto.getNumber());
        Classroom saved = classroomRepo.save(classroom);
        return new ClassroomDTO(saved.getId(), saved.getNumber());
    }

    @PutMapping("/{id}")
    public ClassroomDTO update(@PathVariable Long id, @RequestBody @Valid CreateClassroomDTO dto) {
        Classroom classroom = classroomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
        classroom.setNumber(dto.getNumber());
        Classroom updated = classroomRepo.save(classroom);
        return new ClassroomDTO(updated.getId(), updated.getNumber());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!classroomRepo.existsById(id)) {
            throw new RuntimeException("Classroom not found");
        }
        classroomRepo.deleteById(id);
    }
}