package com.example.projecthub.dto.seller;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
public class SellerSummaryDTO {

    private Long id;
    private String username;
    private String fullName;
    private String profileImgUrl;
    private String status;

    private Long totalProjects;
    private Long totalSales;
    private Double totalRevenue;
    private Double averageRating;

    private LocalDateTime createdAt;

    // ✅ REQUIRED for JPQL "new" keyword
    public SellerSummaryDTO(Long id,
                            String username,
                            String fullName,
                            String profileImgUrl,
                            String status,
                            Long totalProjects,
                            Long totalSales,
                            Double totalRevenue,
                            Double averageRating,
                            LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.profileImgUrl = profileImgUrl;
        this.status = status;
        this.totalProjects = totalProjects;
        this.totalSales = totalSales;
        this.totalRevenue = totalRevenue;
        this.averageRating = averageRating;
        this.createdAt = createdAt;
    }
}