package com.example.repository;

import com.example.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepo extends JpaRepository<Subject, Long> {

    /**
     * Поиск предмета по названию.
     * Полезно для проверки перед созданием нового предмета.
     */
    Optional<Subject> findByName(String name);

    /**
     * Проверка существования предмета по названию.
     */
    boolean existsByName(String name);
}