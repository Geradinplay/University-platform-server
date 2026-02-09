package com.example.controller;

import com.example.dto.CreateSubjectDTO;
import com.example.dto.SubjectDTO;
import com.example.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public List<SubjectDTO> getAll() {
        return subjectService.getAllSubjects();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubjectDTO create(@RequestBody @Valid CreateSubjectDTO dto) {
        return subjectService.createSubject(dto);
    }

    @PutMapping("/{id}")
    public SubjectDTO update(@PathVariable Long id, @RequestBody @Valid CreateSubjectDTO dto) {
        return subjectService.updateSubject(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        subjectService.deleteSubject(id);
    }
}