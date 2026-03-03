package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "archive_task")
public class ArchiveTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_id", nullable = false)
    private Archive archive;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private boolean completed;

    @Column(name = "sort_order")
    private Integer sortOrder;

    public ArchiveTask() {}

    public ArchiveTask(Archive archive, String description, boolean completed, Integer sortOrder) {
        this.archive = archive;
        this.description = description;
        this.completed = completed;
        this.sortOrder = sortOrder;
    }

    public Integer getId() { return id; }
    public Archive getArchive() { return archive; }
    public void setArchive(Archive archive) { this.archive = archive; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}