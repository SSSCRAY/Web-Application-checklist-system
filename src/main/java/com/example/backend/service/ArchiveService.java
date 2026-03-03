package com.example.backend.service;

import com.example.backend.dto.Archive_dto.ArchiveDetailDTO;
import com.example.backend.dto.Archive_dto.ArchiveListDTO;
import com.example.backend.dto.Archive_dto.ArchiveTaskDTO;
import com.example.backend.entity.*;
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
        archive = archiveRepository.save(archive);

        // Копируем задачи в момент архивации — снимок состояния
        List<Task> tasks = taskRepository.findByChecklistIdOrderBySortOrder(checklistId);
        for (Task task : tasks) {
            ArchiveTask archiveTask = new ArchiveTask(
                    archive,
                    task.getDescription(),
                    task.isCompleted(),
                    task.getSortOrder()
            );
            archive.getArchiveTasks().add(archiveTask);
        }

        return archiveRepository.save(archive);
    }

    /**
     * Получить список всех архивных записей
     */
    public List<ArchiveListDTO> getAllArchives() {
        List<Archive> archives = archiveRepository.findAllByOrderBySubmittedAtDesc();
        return archives.stream().map(archive -> {
            int totalTasks = archive.getArchiveTasks().size();
            int completedTasks = (int) archive.getArchiveTasks().stream()
                    .filter(ArchiveTask::isCompleted).count();
            return new ArchiveListDTO(
                    archive.getId(),
                    archive.getChecklistTitle(),
                    archive.getSubmittedBy().getFullName(),
                    archive.getSubmittedAt(),
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

        List<ArchiveTaskDTO> taskDTOs = archive.getArchiveTasks().stream()
                .map(t -> new ArchiveTaskDTO(t.getId(), t.getDescription(), t.isCompleted(), t.getSortOrder()))
                .collect(Collectors.toList());

        return new ArchiveDetailDTO(
                archive.getId(),
                archive.getChecklistTitle(),
                archive.getSubmittedBy().getFullName(),
                archive.getSubmittedAt(),
                taskDTOs
        );
    }
}
