package com.example.backend.reposiroty;

import com.example.backend.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Integer> {

    List<Archive> findAllByOrderBySubmittedAtDesc();

    // Найти все архивные записи связанные с чеклистом
    List<Archive> findAllByChecklistId(Integer checklistId);
}