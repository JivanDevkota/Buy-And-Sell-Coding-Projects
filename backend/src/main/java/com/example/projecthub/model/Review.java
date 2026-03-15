package com.example.projecthub.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false, length = 1000)
    private String comment;

    /**
     * Reference to the project being reviewed.
     * Multiple reviews can reference same project.
     * Cascade delete is handled by Project entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    /**
     * Timestamp when the review was created.
     * Set automatically on entity creation.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the review was last updated.
     * Updated whenever the review is modified.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Lifecycle callback - sets creation timestamp before persisting.
     * Called once when review is first saved to database.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Lifecycle callback - updates modification timestamp before update.
     * Called every time the review is updated in database.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

