package com.example.backend.controllers;

import com.example.backend.dto.Archive_dto.ArchiveDetailDTO;
import com.example.backend.dto.Archive_dto.ArchiveListDTO;
import com.example.backend.dto.Archive_dto.SubmitToArchiveRequest;
import com.example.backend.entity.Archive;
import com.example.backend.security.CustomUserDetails;
import com.example.backend.service.ArchiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/archive")
public class ArchiveController {

    private final ArchiveService archiveService;

    public ArchiveController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @PostMapping
    public ResponseEntity<Archive> submitToArchive(
            @RequestBody SubmitToArchiveRequest request,
            Authentication authentication) {

        // Берём userId из JWT токена — безопасно, пользователь не может подделать
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getId();

        Archive archive = archiveService.submitToArchive(request.checklistId(), userId);
        return ResponseEntity.ok(archive);
    }

    @GetMapping
    public ResponseEntity<List<ArchiveListDTO>> getAllArchives() {
        return ResponseEntity.ok(archiveService.getAllArchives());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArchiveDetailDTO> getArchiveDetails(@PathVariable Integer id) {
        return ResponseEntity.ok(archiveService.getArchiveDetails(id));
    }
}