package com.example.backend.service;

import com.example.backend.dto.Archive_dto.ArchiveDetailDTO;
import com.example.backend.dto.Archive_dto.ArchiveListDTO;
import com.example.backend.dto.Archive_dto.ArchiveTaskDTO;
import com.example.backend.entity.Archive;
import com.example.backend.entity.Checklist;
import com.example.backend.entity.Task;
import com.example.backend.entity.User;
import com.example.backend.mapper.ArchiveMapper;
import com.example.backend.reposiroty.ArchiveRepository;
import com.example.backend.reposiroty.ChecklistRepository;
import com.example.backend.reposiroty.TaskRepository;
import com.example.backend.reposiroty.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArchiveService {

    private final ArchiveRepository archiveRepository;
    private final ChecklistRepository checklistRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ArchiveMapper archiveMapper;

    public ArchiveService(ArchiveRepository archiveRepository,
                          ChecklistRepository checklistRepository,
                          TaskRepository taskRepository,
                          UserRepository userRepository,
                          ArchiveMapper archiveMapper) {
        this.archiveRepository = archiveRepository;
        this.checklistRepository = checklistRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.archiveMapper = archiveMapper;
    }

    /**
     * Отправить чеклист в архив
     */
    @Transactional
    public Archive submitToArchive(Integer checklistId, Integer userId) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Archive archive = new Archive(checklist, checklist.getTitle(), user);

        return archiveRepository.save(archive);
    }

    /**
     * Получить список всех архивных записей
     */
    public List<ArchiveListDTO> getAllArchives() {
        List<Archive> archives = archiveRepository.findAllByOrderBySubmittedAtDesc();

        return archives.stream().map(archive -> {
            ArchiveListDTO baseDTO = archiveMapper.toArchiveListDTO(archive);

            // Считаем задачи
            int totalTasks = 0;
            int completedTasks = 0;

            if (archive.getChecklist() != null) {
                List<Task> tasks = taskRepository.findByChecklistIdOrderBySortOrder(
                        archive.getChecklist().getId()
                );

                totalTasks = tasks.size();
                completedTasks = (int) tasks.stream()
                        .filter(Task::isCompleted)
                        .count();
            }

            // Создаем новый record с посчитанными значениями
            return new ArchiveListDTO(
                    baseDTO.id(),
                    baseDTO.checklistTitle(),
                    baseDTO.submittedByName(),
                    baseDTO.submittedAt(),
                    totalTasks,
                    completedTasks
            );
        }).collect(Collectors.toList());
    }

    /**
     * Получить детали архивной записи
     */
    public ArchiveDetailDTO getArchiveDetails(Integer archiveId) {
        Archive archive = archiveRepository.findById(archiveId)
                .orElseThrow(() -> new RuntimeException("Archive not found"));

        ArchiveDetailDTO baseDTO = archiveMapper.toArchiveDetailDTO(archive);

        // Получаем задачи
        List<ArchiveTaskDTO> taskDTOs = List.of();

        if (archive.getChecklist() != null) {
            List<Task> tasks = taskRepository.findByChecklistIdOrderBySortOrder(
                    archive.getChecklist().getId()
            );

            taskDTOs = archiveMapper.tasksToArchiveTaskDTOs(tasks);
        }

        // Создаем новый record с задачами
        return new ArchiveDetailDTO(
                baseDTO.id(),
                baseDTO.checklistTitle(),
                baseDTO.submittedByName(),
                baseDTO.submittedAt(),
                taskDTOs
        );
    }
}
