package com.example.projecthub.service.review;

import com.example.projecthub.dto.dashboard.DashboardResponse;
import com.example.projecthub.dto.review.ReviewResponseDTO;
import com.example.projecthub.model.Review;

import java.util.List;

public interface ReviewService {

    List<ReviewResponseDTO> getMyReviews(Long userId);

    Review addReview(Long buyerId, Long projectId, int rating, String comment);


//    Double getTotalEarnings();
//    Long getTotalSales();
//    Long getActiveProjects();
//    Double getAverageRating();
}
