package com.example.controller;

import com.example.dto.CreateProfessorDTO;
import com.example.dto.ProfessorDTO;
import com.example.entity.Professor;
import com.example.repository.ProfessorRepo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    private final ProfessorRepo professorRepo;

    public ProfessorController(ProfessorRepo professorRepo) {
        this.professorRepo = professorRepo;
    }

    @GetMapping
    public List<ProfessorDTO> getAll() {
        return professorRepo.findAll().stream()
                .map(p -> new ProfessorDTO(p.getId(), p.getName()))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfessorDTO create(@RequestBody @Valid CreateProfessorDTO dto) {
        Professor professor = new Professor();
        professor.setName(dto.getName());
        Professor saved = professorRepo.save(professor);
        return new ProfessorDTO(saved.getId(), saved.getName());
    }

    @PutMapping("/{id}")
    public ProfessorDTO update(@PathVariable Long id, @RequestBody @Valid CreateProfessorDTO dto) {
        Professor professor = professorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor not found"));
        professor.setName(dto.getName());
        Professor updated = professorRepo.save(professor);
        return new ProfessorDTO(updated.getId(), updated.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!professorRepo.existsById(id)) {
            throw new RuntimeException("Professor not found");
        }
        professorRepo.deleteById(id);
    }
}