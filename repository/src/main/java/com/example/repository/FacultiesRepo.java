package com.example.repository;

import com.example.entity.Classroom;
import com.example.entity.Faculties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultiesRepo extends JpaRepository<Faculties, Long> {
}
