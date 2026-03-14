package com.example.backend.service;

import com.example.backend.reposiroty.ArchiveRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ArchiveCleanupService {

    private final ArchiveRepository archiveRepository;

    public ArchiveCleanupService(ArchiveRepository archiveRepository) {
        this.archiveRepository = archiveRepository;
    }

    // Каждую ночь в 03:00 удаляет архивы старше 30 дней
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void deleteOldArchives() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        archiveRepository.deleteAllBySubmittedAtBefore(cutoff);
    }
}
