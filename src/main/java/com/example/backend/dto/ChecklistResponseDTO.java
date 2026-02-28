package com.example.backend.dto;



import java.time.LocalDateTime;
import java.util.List;


public record ChecklistResponseDTO(
        Integer id,
        String title,
        List<TaskResponseDTO> tasks,
        LocalDateTime createdAt
) {

}
