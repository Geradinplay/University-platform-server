package com.example.service;

import com.example.dto.CreateSubjectDTO;
import com.example.dto.SubjectDTO;
import com.example.entity.Subject;
import com.example.repository.SubjectRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepo subjectRepo;

    @Transactional(readOnly = true)
    public List<SubjectDTO> getAllSubjects() {
        return subjectRepo.findAll().stream()
                .map(s -> new SubjectDTO(s.getId(), s.getName()))
                .toList();
    }

    public SubjectDTO createSubject(CreateSubjectDTO dto) {
        if (subjectRepo.existsByName(dto.getName())) {
            throw new IllegalStateException("Предмет с таким названием уже существует");
        }
        Subject subject = new Subject();
        subject.setName(dto.getName());
        Subject saved = subjectRepo.save(subject);
        return new SubjectDTO(saved.getId(), saved.getName());
    }

    public SubjectDTO updateSubject(Long id, CreateSubjectDTO dto) {
        Subject subject = subjectRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Предмет не найден"));

        if (!subject.getName().equals(dto.getName()) && subjectRepo.existsByName(dto.getName())) {
            throw new IllegalStateException("Предмет с таким названием уже существует");
        }

        subject.setName(dto.getName());
        Subject updated = subjectRepo.save(subject);
        return new SubjectDTO(updated.getId(), updated.getName());
    }

    public void deleteSubject(Long id) {
        if (!subjectRepo.existsById(id)) {
            throw new IllegalArgumentException("Предмет не найден");
        }
        subjectRepo.deleteById(id);
    }
    public List<Subject> getAll() {
        return subjectRepo.findAll();
    }

    public Subject save(Subject subject) {
        return subjectRepo.save(subject);
    }
}