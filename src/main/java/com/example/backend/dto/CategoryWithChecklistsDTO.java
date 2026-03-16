package com.example.backend.dto;

import java.util.List;

public record CategoryWithChecklistsDTO(
        Integer id,
        String name,
        Integer parentId,
        List<ChecklistResponseDTO> checklists,
        List<CategoryWithChecklistsDTO> children
) {}