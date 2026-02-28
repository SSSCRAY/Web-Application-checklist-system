package com.example.backend.dto;



public record TaskResponseDTO(
        Integer id,
        String description,
        Integer sortOrder,
        boolean completed) {
}
