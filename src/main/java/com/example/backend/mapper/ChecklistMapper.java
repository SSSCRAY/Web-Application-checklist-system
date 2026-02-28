package com.example.backend.mapper;

import com.example.backend.dto.ChecklistCreateDTO;
import com.example.backend.dto.ChecklistResponseDTO;
import com.example.backend.entity.Checklist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChecklistMapper {

    // Для создания названия чек листа (Админ создает чек-лист, а маппер преобразовывает его в Entity)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Checklist toEntity(ChecklistCreateDTO createDTO);

    // Для получение чек листа на странице то что пользователь/админ видит (id, title, task) - отдаем пользователю
    ChecklistResponseDTO toResponseDTO(Checklist checklist);
}
