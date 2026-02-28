package com.example.backend.mapper;

import com.example.backend.dto.Archive_dto.ArchiveDetailDTO;
import com.example.backend.dto.Archive_dto.ArchiveListDTO;
import com.example.backend.dto.Archive_dto.ArchiveTaskDTO;
import com.example.backend.entity.Archive;
import com.example.backend.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArchiveMapper {

    // Task -> ArchiveTaskDTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "completed", target = "completed")
    @Mapping(source = "sortOrder", target = "sortOrder")
    ArchiveTaskDTO taskToArchiveTaskDTO(Task task);

    // List<Task> -> List<ArchiveTaskDTO>
    List<ArchiveTaskDTO> tasksToArchiveTaskDTOs(List<Task> tasks);

    // Archive -> ArchiveDetailDTO (без задач - добавим вручную)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "checklistTitle", target = "checklistTitle")
    @Mapping(source = "submittedBy.fullName", target = "submittedByName")
    @Mapping(source = "submittedAt", target = "submittedAt")
    @Mapping(target = "tasks", ignore = true)
    ArchiveDetailDTO toArchiveDetailDTO(Archive archive);

    // Archive -> ArchiveListDTO (без подсчета задач)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "checklistTitle", target = "checklistTitle")
    @Mapping(source = "submittedBy.fullName", target = "submittedByName")
    @Mapping(source = "submittedAt", target = "submittedAt")
    @Mapping(target = "totalTasks", ignore = true)
    @Mapping(target = "completedTasks", ignore = true)
    ArchiveListDTO toArchiveListDTO(Archive archive);
}
