package com.example.projecthub.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewStatsDTO {
    private Double avgRating;
    private long totalReviews;
    private long fiveStarReviews;
    private double fiveStarPercentage;
    private double responseRate;
    private long pendingResponses;
}
