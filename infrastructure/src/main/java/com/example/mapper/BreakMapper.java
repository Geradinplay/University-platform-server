package com.example.mapper;

import com.example.dto.BreakDTO;
import com.example.entity.Break;
import org.springframework.stereotype.Component;

@Component
public class BreakMapper {

    /**
     * Преобразует DTO в Entity.
     */
    public Break toEntity(BreakDTO dto) {
        if (dto == null) {
            return null;
        }

        Break breakEntity = new Break();

        // Маппим ID (важно для Update операций)
        if (dto.getId() != null) {
            breakEntity.setId(dto.getId());
        }

        // Переносим новые поля времени
        breakEntity.setStartTime(dto.getStartTime());
        breakEntity.setEndTime(dto.getEndTime());
        breakEntity.setDay(dto.getDay());

        return breakEntity;
    }

    /**
     * Преобразует Entity обратно в DTO.
     */
    public BreakDTO toDto(Break entity) {
        if (entity == null) {
            return null;
        }

        BreakDTO dto = new BreakDTO();
        dto.setId(entity.getId());

        // Маппим поля времени обратно
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setDay(entity.getDay());

        return dto;
    }
}