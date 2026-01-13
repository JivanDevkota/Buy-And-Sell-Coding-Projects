package com.example.projecthub.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Download {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "project_file_id",nullable = false)
    private ProjectFile projectFile;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    private LocalDateTime downloadedAt;

    private String ipAddress;
    private String userAgent;

    @PrePersist
    public void onCreate(){
        this.downloadedAt = LocalDateTime.now();
    }

}
