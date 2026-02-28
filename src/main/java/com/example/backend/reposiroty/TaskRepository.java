package com.example.backend.reposiroty;

import com.example.backend.entity.Checklist;
import com.example.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    // Этот метод для проверки что Task принадлежит Checklist
    Optional<Task> findByIdAndChecklistId(Integer taskId, Integer checklistId);

    // Получить все задачи чеклиста
    List<Task> findByChecklistIdOrderBySortOrder(Integer checklistId);
}
