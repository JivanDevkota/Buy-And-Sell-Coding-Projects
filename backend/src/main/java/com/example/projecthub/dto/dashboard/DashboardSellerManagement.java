package com.example.projecthub.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardSellerManagement {
    private double totalRevenue;
    private String topSeller;
    private int avgRating;
    private long totalProjects;
}
