package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskCreateDTO(
        @NotBlank(message = "Описание задачи не может быть пустым")
        @Size(min = 2, max = 500, message = "Описание от 2 до 500 символов")
        String description,

        Integer sortOrder
) {}