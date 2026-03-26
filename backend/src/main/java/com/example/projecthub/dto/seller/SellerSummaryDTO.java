package com.example.projecthub.dto.seller;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class SellerSummaryDTO {
    private Long id;
    private String username;
    private String fullName;
    private String profileImgUrl;
    private String status;          // PENDING | ACTIVE | SUSPENDED
    private int totalProjects;
    private int totalSales;         // sum of purchaseCount across seller's projects
    private double totalRevenue;    // sum of paidAmount from completed purchases
    private double averageRating;   // weighted avg across all projects
    private LocalDateTime createdAt;
}
