package com.example.backend.service;

import com.example.backend.dto.ChecklistCreateDTO;
import com.example.backend.dto.ChecklistResponseDTO;
import com.example.backend.entity.Archive;
import com.example.backend.entity.Checklist;
import com.example.backend.mapper.ChecklistMapper;
import com.example.backend.reposiroty.ArchiveRepository;
import com.example.backend.reposiroty.ChecklistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final ArchiveRepository archiveRepository;
    private final ChecklistMapper mapper;

    public ChecklistService(ChecklistRepository checklistRepository,
                            ArchiveRepository archiveRepository,
                            ChecklistMapper mapper) {
        this.checklistRepository = checklistRepository;
        this.archiveRepository = archiveRepository;
        this.mapper = mapper;
    }

    @Transactional
    public ChecklistResponseDTO create(ChecklistCreateDTO createDTO) {
        Checklist checklist = mapper.toEntity(createDTO);
        Checklist saved = checklistRepository.save(checklist);
        return mapper.toResponseDTO(saved);
    }

    @Transactional
    public List<ChecklistResponseDTO> getChecklistAll() {
        return checklistRepository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChecklistResponseDTO getChecklistId(Integer id) {
        Checklist checklist = checklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));
        return mapper.toResponseDTO(checklist);
    }

    @Transactional
    public ChecklistResponseDTO updated(Integer id, ChecklistCreateDTO createDTO) {
        Checklist checklist = checklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));
        checklist.setTitle(createDTO.title());
        return mapper.toResponseDTO(checklist);
    }

    @Transactional
    public void delete(Integer id) {
        Checklist checklist = checklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));

        // Обнуляем ссылку на чеклист в архиве — записи архива остаются
        List<Archive> archives = archiveRepository.findAllByChecklistId(id);
        archives.forEach(a -> a.setChecklist(null));
        archiveRepository.saveAll(archives);

        checklistRepository.delete(checklist);
    }
}