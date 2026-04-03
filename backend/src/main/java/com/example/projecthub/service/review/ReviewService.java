package com.example.projecthub.service.review;

import com.example.projecthub.dto.review.ReviewResponseDTO;
import com.example.projecthub.dto.review.ReviewStatsDTO;
import com.example.projecthub.model.Review;

import java.util.List;

public interface ReviewService {

    List<ReviewResponseDTO> getMyReviews(Long userId);

    Review addReview(Long buyerId, Long projectId, int rating, String comment);

    ReviewStatsDTO getReviewStats(Long sellerId);

//    Double getTotalEarnings();
//    Long getTotalSales();
//    Long getActiveProjects();
//    Double getAverageRating();
}
