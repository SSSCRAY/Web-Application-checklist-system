package com.example.backend.service;

import com.example.backend.dto.CategoryDTO;
import com.example.backend.dto.CategoryWithChecklistsDTO;
import com.example.backend.dto.ChecklistResponseDTO;
import com.example.backend.entity.Category;
import com.example.backend.entity.Checklist;
import com.example.backend.mapper.ChecklistMapper;
import com.example.backend.reposiroty.CategoryRepository;
import com.example.backend.reposiroty.ChecklistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ChecklistRepository checklistRepository;
    private final ChecklistMapper checklistMapper;

    public CategoryService(CategoryRepository categoryRepository,
                           ChecklistRepository checklistRepository,
                           ChecklistMapper checklistMapper) {
        this.categoryRepository = categoryRepository;
        this.checklistRepository = checklistRepository;
        this.checklistMapper = checklistMapper;
    }

    @Transactional
    public CategoryDTO create(String name) {
        Category category = categoryRepository.save(new Category(name));
        return new CategoryDTO(category.getId(), category.getName());
    }

    @Transactional
    public CategoryDTO rename(Integer id, String name) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(name);
        return new CategoryDTO(category.getId(), category.getName());
    }

    @Transactional
    public void delete(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        // Убираем категорию у чек-листов
        category.getChecklists().forEach(c -> c.setCategory(null));
        checklistRepository.saveAll(category.getChecklists());
        categoryRepository.delete(category);
    }

    public List<CategoryWithChecklistsDTO> getAllWithChecklists() {
        return categoryRepository.findAll().stream().map(cat -> {
            List<ChecklistResponseDTO> cls = cat.getChecklists().stream()
                    .map(checklistMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new CategoryWithChecklistsDTO(cat.getId(), cat.getName(), cls);
        }).collect(Collectors.toList());
    }

    @Transactional
    public void assignChecklist(Integer categoryId, Integer checklistId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));
        checklist.setCategory(category);
        checklistRepository.save(checklist);
    }
}
