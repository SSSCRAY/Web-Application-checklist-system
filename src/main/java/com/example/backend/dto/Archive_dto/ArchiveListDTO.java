package com.example.backend.dto.Archive_dto;

import java.time.LocalDateTime;

public record ArchiveListDTO(
        Integer id,
        String checklistTitle,
        String submittedByName,
        LocalDateTime submittedAt,
        Integer totalTasks,
        Integer completedTasks
) {
}
