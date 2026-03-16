package com.example.backend.controllers;

import com.example.backend.dto.CategoryDTO;
import com.example.backend.dto.CategoryWithChecklistsDTO;
import com.example.backend.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryWithChecklistsDTO>> getAll() {
        return ResponseEntity.ok(categoryService.getAllWithChecklists());
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> create(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        Integer parentId = body.get("parentId") != null
                ? ((Number) body.get("parentId")).intValue()
                : null;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.create(name, parentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> rename(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(categoryService.rename(id, body.get("name")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{categoryId}/checklists/{checklistId}")
    public ResponseEntity<Void> assignChecklist(
            @PathVariable Integer categoryId,
            @PathVariable Integer checklistId) {
        categoryService.assignChecklist(categoryId, checklistId);
        return ResponseEntity.noContent().build();
    }
}