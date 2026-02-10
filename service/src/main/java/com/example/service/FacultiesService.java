package com.example.service;

import com.example.dto.FacultyDTO;
import com.example.dto.CreateFacultyDTO;
import com.example.entity.Faculties;
import com.example.mapper.FacultyMapper;
import com.example.repository.FacultiesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacultiesService {

    private final FacultiesRepo repository;
    private final FacultyMapper mapper;

    @Transactional(readOnly = true)
    public List<FacultyDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FacultyDTO getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));
    }

    @Transactional
    public FacultyDTO create(CreateFacultyDTO dto) {
        Faculties entity = new Faculties();
        entity.setName(dto.getName());
        entity.setShort_name(dto.getShortName());
        Faculties saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Transactional
    public FacultyDTO update(Long id, CreateFacultyDTO dto) {
        return repository.findById(id)
                .map(entity -> {
                    entity.setName(dto.getName());
                    entity.setShort_name(dto.getShortName());
                    return mapper.toDTO(repository.save(entity));
                })
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Faculty not found with id: " + id);
        }
        repository.deleteById(id);
    }
}