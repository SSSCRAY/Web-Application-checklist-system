package com.example.backend.controllers;

import com.example.backend.dto.ChecklistCreateDTO;
import com.example.backend.dto.ChecklistResponseDTO;
import com.example.backend.service.ChecklistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checklists")
public class ChecklistController {

    private final ChecklistService checklistService;

    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @PostMapping
    public ResponseEntity<ChecklistResponseDTO> create(@Valid @RequestBody ChecklistCreateDTO createDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checklistService.create(createDTO));
    }

    @GetMapping
    public ResponseEntity<List<ChecklistResponseDTO>> getChecklistAll() {
        return ResponseEntity.ok(checklistService.getChecklistAll());
    }

    @GetMapping("/{checklistId}")
    public ResponseEntity<ChecklistResponseDTO> getChecklistId(@PathVariable Integer checklistId) {
        ChecklistResponseDTO dto = checklistService.getChecklistId(checklistId);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{checklistId}")
    public ResponseEntity<ChecklistResponseDTO> update(
            @PathVariable Integer checklistId,
            @Valid @RequestBody ChecklistCreateDTO createDTO) {
        return ResponseEntity.ok(checklistService.updated(checklistId, createDTO));
    }

    @DeleteMapping("/{checklistId}")
    public ResponseEntity<Void> delete(@PathVariable Integer checklistId) {
        checklistService.delete(checklistId);
        return ResponseEntity.noContent().build();
    }
}