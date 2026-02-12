package com.example.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "faculty_id", nullable = false)
    private Long facultyId;

    @Column(nullable = false)
    private Integer semester;

    @Column(nullable = false)
    private Boolean isExam = false;

    // Связь с уроками - при удалении расписания удаляются все уроки
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    // Связь с перерывами - при удалении расписания удаляются все перерывы
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Break> breaks = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getFacultyId() { return facultyId; }
    public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public Boolean getIsExam() { return isExam; }
    public void setIsExam(Boolean isExam) { this.isExam = isExam; }

    public List<Lesson> getLessons() { return lessons; }
    public void setLessons(List<Lesson> lessons) { this.lessons = lessons; }

    public List<Break> getBreaks() { return breaks; }
    public void setBreaks(List<Break> breaks) { this.breaks = breaks; }
}
