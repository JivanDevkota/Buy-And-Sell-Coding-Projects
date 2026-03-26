package com.example.projecthub.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardResponse {

    private Double totalEarnings;
    private Double earningsChange;

    private Long totalSales;
    private Double salesChange;

    private Long activeProjects;

    private Double averageRating;
    private Double ratingChange;
}