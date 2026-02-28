package com.example.backend.dto.Archive_dto;

import java.time.LocalDateTime;
import java.util.List;

public record ArchiveDetailDTO(
        Integer id,
        String checklistTitle,
        String submittedByName,
        LocalDateTime submittedAt,
        List<ArchiveTaskDTO>tasks
) {
}
