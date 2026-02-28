package com.example.backend.dto.Archive_dto;

public record ArchiveTaskDTO(
        Integer id,
        String description,
        Boolean completed,
        Integer sortOrder
) {
}
