package com.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalTime;

@Data
public class LessonDTO {
    public interface CreateGroup {}
    public interface UpdateGroup {}

    private Long id;

    @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
    @Min(0) @Max(7)
    private Integer day;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SubjectDTO subject;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ProfessorDTO professor;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ClassroomDTO classroom;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(groups = CreateGroup.class)
    private Long subjectId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(groups = CreateGroup.class)
    private Long professorId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(groups = CreateGroup.class)
    private Long classroomId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(groups = CreateGroup.class)
    private Long scheduleId;
}