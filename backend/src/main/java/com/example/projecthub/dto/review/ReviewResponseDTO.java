package com.example.projecthub.dto.review;

import lombok.Data;

@Data
public class ReviewResponseDTO {
    private Long reviewId;
    private String projectName;
    private int rating;
    private String comment;
    private String reviewerName;
    private String reviewerInitials;
    private String timeAgo;

    private String sellerResponse;
    private String sellerResponseTime;

}
