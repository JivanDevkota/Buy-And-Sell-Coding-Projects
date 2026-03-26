package com.example.projecthub.dto.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellerStatsDTO {
    private long totalSellers;
    private long verifiedSellers;
    private long pendingSellers;
    private long suspendedSellers;
    private double totalRevenue;
    private String topSellerName;
    private double topSellerRevenue;
    private double averageRating;
    private long totalProjects;
}
