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

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ChecklistRepository checklistRepository;
    private final TaskMapper mapper;

    public TaskService(TaskRepository taskRepository, ChecklistRepository checklistRepository, TaskMapper mapper) {
        this.taskRepository = taskRepository;
        this.checklistRepository = checklistRepository;
        this.mapper = mapper;
    }


    // Создать задачу в чек листе
    @Transactional
    public TaskResponseDTO create(Integer checklistId, TaskCreateDTO createDTO) {
        // Находим чек лист
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

        Task saved = taskRepository.save(task);

        return mapper.toDTO(saved);

    }


    @Transactional
    public TaskResponseDTO update(Integer checklistId, Integer taskId, TaskUpdateDTO updateDTO) {
        Task task = taskRepository.findByIdAndChecklistId(taskId, checklistId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setDescription(updateDTO.description());
        Task updated = taskRepository.save(task);

        return mapper.toDTO(updated);
    }

    @Transactional
    public void delete(Integer taskId) {
        Task task = taskRepository.findById(taskId).
                orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(task);
    }

    public TaskResponseDTO toggle(Integer taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setCompleted(!task.isCompleted());
        Task updated = taskRepository.save(task);
        return mapper.toDTO(updated);
    }
}
