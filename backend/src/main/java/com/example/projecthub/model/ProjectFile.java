package com.example.projecthub.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "project_files")
public class ProjectFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileUrl;
    private String fileSize;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    private String description;


    @ManyToOne
    @JoinColumn(name = "project_id",nullable = false)
    private Project project;

    private LocalDateTime updatedAt;

    private Boolean isActive=true;

    private Integer displayOrder=0;

    // ProjectFile.java — add this field
    @Column(name = "download_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer downloadCount = 0;

    @PrePersist
    public void onCreate() {
        updatedAt = LocalDateTime.now();
    }

}
