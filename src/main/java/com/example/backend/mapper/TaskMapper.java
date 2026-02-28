package com.example.backend.mapper;

import com.example.backend.dto.TaskCreateDTO;
import com.example.backend.dto.TaskResponseDTO;
import com.example.backend.dto.TaskUpdateDTO;
import com.example.backend.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {


    // Этот маппер для отдачи клиенту данных из entity
    // Entity -> DTO
    /*
    Превращаем объект из базы данных (Task) в объект для отправки клиенту (TaskResponseDTO2)

    база данных -> Entity -> mapper(превращаем entity в dto чтобы отдать данные клиенту которые в dto) -> сайт/приложение
     */
// Маппинг Entity -> Response DTO
    // В ResponseDTO только те поля, которые нужны клиенту
    @Mapping(target = "id", ignore = true) // если id не нужно маппить (например, автоинкремент)
    TaskResponseDTO toDTO(Task task);
    // Обратите внимание: createdAt, updatedAt, checklist НЕ УКАЗЫВАЕМ,
    // так как их нет в TaskResponseDTO

    // Маппинг CreateDTO -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "checklist", ignore = true) // если есть в Task, но не маппим из DTO
    @Mapping(target = "createdAt", ignore = true) // устанавливается автоматически в базе/сервисе
    @Mapping(target = "updatedAt", ignore = true) // устанавливается автоматически
    @Mapping(target = "completed", constant = "false") // значение по умолчанию
    Task toEntity(TaskCreateDTO createDTO);

    // Обновление существующей Entity из UpdateDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "checklist", ignore = true)
    @Mapping(target = "sortOrder", ignore = true) // Не обновляем сортировку
    @Mapping(target = "createdAt", ignore = true) // Не обновляем дату создания
    @Mapping(target = "updatedAt", ignore = true) // Установите в сервисе перед save
    @Mapping(target = "completed", source = "completed")
    void updateFromDTO(TaskUpdateDTO updateDTO, @MappingTarget Task task);
}
