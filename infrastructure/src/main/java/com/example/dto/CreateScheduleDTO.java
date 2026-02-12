package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateScheduleDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Long facultyId;
    @NotNull
    private Integer semester;
    @NotNull
    private Boolean isExam = false;

    public CreateScheduleDTO() {}
    public CreateScheduleDTO(Long id, String name, Long facultyId, Integer semester, Boolean isExam) {
        this.id = id; this.name = name; this.facultyId = facultyId; this.semester = semester; this.isExam = isExam;
    }

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
}
