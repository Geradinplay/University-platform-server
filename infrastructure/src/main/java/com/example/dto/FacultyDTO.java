package com.example.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDTO {

    private Long id;

    @NotBlank(message = "Название факультета не может быть пустым")
    @Size(min = 2, max = 150, message = "Название должно быть от 2 до 150 символов")
    private String name;

    @NotBlank(message = "Короткое название обязательно")
    @Size(min = 1, max = 20, message = "Сокращение должно быть от 1 до 20 символов")
    private String shortName;
}