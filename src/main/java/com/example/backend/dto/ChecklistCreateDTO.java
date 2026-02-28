package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChecklistCreateDTO(
        @NotBlank(message = "Название не может быть пустым")
        @Size(min = 2, max = 255, message = "Название от 2 до 255 символов")
        String title
) {}
