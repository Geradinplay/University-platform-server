package com.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalTime;

@Data
public class BreakDTO {

    private Long id;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @NotNull
    @Min(0) @Max(7)
    private Integer day;
}