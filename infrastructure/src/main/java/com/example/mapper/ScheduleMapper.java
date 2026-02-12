package com.example.mapper;

import com.example.dto.CreateScheduleDTO;
import com.example.dto.ScheduleDTO;
import com.example.entity.Schedule;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {

    public ScheduleDTO toDTO(Schedule entity) {
        if (entity == null) return null;
        return new ScheduleDTO(
                entity.getId(),
                entity.getName(),
                entity.getFacultyId(),
                entity.getSemester(),
                entity.getIsExam()
        );
    }

    public Schedule toEntity(CreateScheduleDTO dto) {
        if (dto == null) return null;
        Schedule entity = new Schedule();
        entity.setName(dto.getName());
        entity.setFacultyId(dto.getFacultyId());
        entity.setSemester(dto.getSemester());
        entity.setIsExam(dto.getIsExam());
        return entity;
    }

    public Schedule toEntity(ScheduleDTO dto) {
        if (dto == null) return null;
        Schedule entity = new Schedule();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setFacultyId(dto.getFacultyId());
        entity.setSemester(dto.getSemester());
        entity.setIsExam(dto.getIsExam());
        return entity;
    }
}

