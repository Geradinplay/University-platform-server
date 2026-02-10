package com.example.repository;

import com.example.entity.Break;
import com.example.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreakRepo extends JpaRepository<Break, Long> {
    List<Break> findByDay(Integer day);

    /**
     * Получить все перерывы конкретного расписания
     */
    List<Break> findByScheduleId(Long scheduleId);
}
