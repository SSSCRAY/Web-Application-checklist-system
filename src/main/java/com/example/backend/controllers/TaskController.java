package com.example.backend.controllers;

import com.example.backend.dto.TaskCreateDTO;
import com.example.backend.dto.TaskResponseDTO;
import com.example.backend.dto.TaskUpdateDTO;
import com.example.backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checklists/{checklistId}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> addTask(
            @PathVariable Integer checklistId,
            @Valid @RequestBody TaskCreateDTO createDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(checklistId, createDTO));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updatedTask(
            @PathVariable Integer checklistId,
            @PathVariable Integer taskId,
            @Valid @RequestBody TaskUpdateDTO updateDTO) {
        return ResponseEntity.ok(taskService.update(checklistId, taskId, updateDTO));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer taskId) {
        taskService.delete(taskId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TaskResponseDTO> toggleTask(@PathVariable Integer id) {
        return ResponseEntity.ok(taskService.toggle(id));
    }

    // Сброс всех галочек — новая смена
    @PostMapping("/reset")
    public ResponseEntity<Void> resetAll(@PathVariable Integer checklistId) {
        taskService.resetAll(checklistId);
        return ResponseEntity.noContent().build();
    }
}