package com.example.repository;

import com.example.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface LessonRepo extends JpaRepository<Lesson, Long> {

    List<Lesson> findByDay(Integer day);

    /**
     * Проверка на точное совпадение времени.
     * Заменил String time на два поля LocalTime.
     */
    boolean existsByDayAndStartTimeAndEndTime(Integer day, LocalTime startTime, LocalTime endTime);

}