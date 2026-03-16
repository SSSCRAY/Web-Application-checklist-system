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
    public CategoryDTO create(String name, Integer parentId) {
        Category parent = null;
        if (parentId != null) {
            parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            // Проверяем глубину — максимум 3 уровня
            int depth = getDepth(parent);
            if (depth >= 2) throw new RuntimeException("Максимум 3 уровня вложенности");
        }
        Category category = categoryRepository.save(new Category(name, parent));
        return new CategoryDTO(category.getId(), category.getName(),
                parent != null ? parent.getId() : null);
    }

    private int getDepth(Category cat) {
        int depth = 0;
        Category current = cat;
        while (current.getParent() != null) {
            depth++;
            current = current.getParent();
        }
        return depth;
    }

    @Transactional
    public CategoryDTO rename(Integer id, String name) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(name);
        return new CategoryDTO(category.getId(), category.getName(),
                category.getParent() != null ? category.getParent().getId() : null);
    }

    @Transactional
    public void delete(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.getChecklists().forEach(c -> c.setCategory(null));
        checklistRepository.saveAll(category.getChecklists());
        categoryRepository.delete(category);
    }

    // Возвращает только корневые папки с детьми внутри
    public List<CategoryWithChecklistsDTO> getAllWithChecklists() {
        List<Category> roots = categoryRepository.findAll().stream()
                .filter(c -> c.getParent() == null)
                .collect(Collectors.toList());
        return roots.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private CategoryWithChecklistsDTO toDTO(Category cat) {
        List<ChecklistResponseDTO> cls = cat.getChecklists().stream()
                .map(checklistMapper::toResponseDTO)
                .collect(Collectors.toList());
        List<CategoryWithChecklistsDTO> children = cat.getChildren().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new CategoryWithChecklistsDTO(
                cat.getId(), cat.getName(),
                cat.getParent() != null ? cat.getParent().getId() : null,
                cls, children);
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