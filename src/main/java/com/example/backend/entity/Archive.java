package com.example.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "archive")
public class Archive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "checklist_id")
    private Checklist checklist; // может быть null если чеклист удалили

    @Column(name = "checklist_title", nullable = false, length = 255)
    private String checklistTitle; // копия названия

    @ManyToOne
    @JoinColumn(name = "submitted_by", nullable = false)
    private User submittedBy; // кто отправил

    @CreationTimestamp
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;


    public Archive() {}

    public Archive(Checklist checklist, String checklistTitle, User submittedBy) {
        this.checklist = checklist;
        this.checklistTitle = checklistTitle;
        this.submittedBy = submittedBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public String getChecklistTitle() {
        return checklistTitle;
    }

    public void setChecklistTitle(String checklistTitle) {
        this.checklistTitle = checklistTitle;
    }

    public User getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(User submittedBy) {
        this.submittedBy = submittedBy;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
