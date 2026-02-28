package com.example.backend.service;

import com.example.backend.dto.TaskCreateDTO;
import com.example.backend.dto.TaskResponseDTO;
import com.example.backend.dto.TaskUpdateDTO;
import com.example.backend.entity.Checklist;
import com.example.backend.entity.Task;
import com.example.backend.mapper.TaskMapper;
import com.example.backend.reposiroty.ChecklistRepository;
import com.example.backend.reposiroty.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ChecklistRepository checklistRepository;
    private final TaskMapper mapper;

    public TaskService(TaskRepository taskRepository,
                       ChecklistRepository checklistRepository,
                       TaskMapper mapper) {
        this.taskRepository = taskRepository;
        this.checklistRepository = checklistRepository;
        this.mapper = mapper;
    }

    @Transactional
    public TaskResponseDTO create(Integer checklistId, TaskCreateDTO createDTO) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));

        Task task = mapper.toEntity(createDTO);
        task.setChecklist(checklist);

        if (task.getSortOrder() == null) {
            int maxOrder = checklist.getTasks()
                    .stream()
                    .mapToInt(Task::getSortOrder)
                    .max()
                    .orElse(-1);
            task.setSortOrder(maxOrder + 1);
        }

        return mapper.toDTO(taskRepository.save(task));
    }

    @Transactional
    public TaskResponseDTO update(Integer checklistId, Integer taskId, TaskUpdateDTO updateDTO) {
        Task task = taskRepository.findByIdAndChecklistId(taskId, checklistId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setDescription(updateDTO.description());
        return mapper.toDTO(taskRepository.save(task));
    }

    @Transactional
    public void delete(Integer taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }

    @Transactional
    public TaskResponseDTO toggle(Integer taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setCompleted(!task.isCompleted());
        return mapper.toDTO(taskRepository.save(task));
    }

    // Сброс всех галочек чеклиста — для новой смены
    @Transactional
    public void resetAll(Integer checklistId) {
        checklistRepository.findById(checklistId)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));

        List<Task> tasks = taskRepository.findByChecklistIdOrderBySortOrder(checklistId);
        tasks.forEach(t -> t.setCompleted(false));
        taskRepository.saveAll(tasks);
    }
}