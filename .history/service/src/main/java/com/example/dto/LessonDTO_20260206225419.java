package com.example.dto;

import javax.validation.constraints.NotBlank;

public class LessonDTO {
    private Long id;

    @NotBlank(message = "Lesson name cannot be blank")
    private String name;

    private String description;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
