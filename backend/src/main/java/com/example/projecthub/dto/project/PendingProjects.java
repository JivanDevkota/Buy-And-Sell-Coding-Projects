package com.example.projecthub.dto.project;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PendingProjects {
    private Long projectId;
    private String projectName;
    private String sellerName;
    private String categoryName;
    private LocalDateTime submittedAt;
    private Double price;


    public PendingProjects(Long projectId, String projectName, String sellerName, String categoryName, LocalDateTime submittedAt, Double price) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.sellerName = sellerName;
        this.categoryName = categoryName;
        this.submittedAt = submittedAt;
        this.price = price;
    }
}
