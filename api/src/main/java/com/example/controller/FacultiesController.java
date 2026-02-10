package com.example.controller;

import com.example.dto.FacultyDTO;
import com.example.dto.CreateFacultyDTO;
import com.example.service.FacultiesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculties")
@RequiredArgsConstructor
public class FacultiesController {

    private final FacultiesService service;

    @GetMapping
    public List<FacultyDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public FacultyDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FacultyDTO create(@Valid @RequestBody CreateFacultyDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public FacultyDTO update(@PathVariable Long id, @Valid @RequestBody CreateFacultyDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}