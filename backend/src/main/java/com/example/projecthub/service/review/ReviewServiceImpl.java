package com.example.projecthub.service.review;

import com.example.projecthub.dto.review.ReviewResponseDTO;
import com.example.projecthub.dto.review.ReviewStatsDTO;
import com.example.projecthub.model.Project;
import com.example.projecthub.model.PurchaseStatus;
import com.example.projecthub.model.Review;
import com.example.projecthub.model.User;
import com.example.projecthub.repository.ProjectRepository;
import com.example.projecthub.repository.PurchaseRepository;
import com.example.projecthub.repository.ReviewRepository;
import com.example.projecthub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final PurchaseRepository purchaseRepository;

    @Transactional
    public Review addReview(Long buyerId, Long projectId, int rating, String comment) {
        log.info("Adding review for project: {} by buyer: {} with rating: {}", projectId, buyerId, rating);

        // Validation: Rating range check (must be done early fo
        // r quick failure)
        if (rating < 1 || rating > 5) {
            log.warn("Invalid rating: {} for buyer: {} on project: {}", rating, buyerId, projectId);
            throw new RuntimeException("Invalid rating: " + rating + ". Rating must be between 1 and 5.");
        }

        // Validation: Buyer exists
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found with ID: " + buyerId));

        // Validation: Project exists
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        // Validation: Buyer has purchased the project
        boolean hasPurchased = purchaseRepository
                .existsByBuyerIdAndProjectIdAndStatus(buyerId, projectId, PurchaseStatus.COMPLETED);
        if (!hasPurchased) {
            log.warn("Buyer: {} attempted to review project: {} without purchasing", buyerId, projectId);
            throw new RuntimeException("You cannot review this project because you haven't purchased it yet.");
        }

        // Validation: Buyer hasn't already reviewed this project
        boolean alreadyReviewed = reviewRepository.existsByUserIdAndProjectId(buyerId, projectId);
        if (alreadyReviewed) {
            log.warn("Buyer: {} already reviewed project: {}", buyerId, projectId);
            throw new RuntimeException("You have already reviewed this project. Each user can leave only one review per project.");
        }

        // Create review entity
        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setProject(project);
        review.setUser(buyer);

        // Save review and update project rating
        Review savedReview = reviewRepository.save(review);

        // Update project with new review (triggers rating recalculation)
        project.addReview(savedReview);
        projectRepository.save(project);

        log.info("Review successfully added. ID: {} for project: {} by buyer: {}",
                savedReview.getId(), projectId, buyerId);

        return savedReview;
    }

    @Override
    public ReviewStatsDTO getReviewStats(Long sellerId) {
        long totalReview = reviewRepository.countBySellerId(sellerId);
        long fiveStarReviews = reviewRepository.countFiveStarBySellerId(sellerId);
        long pendingResponses = reviewRepository.countPendingResponseBySellerId(sellerId);
        Double avgRating = reviewRepository.avgRatingBySellerId(sellerId);

        double fiveStarPercentage = totalReview > 0 ? (fiveStarReviews * 100.0 / totalReview) : 0.0;
        double responseRate = totalReview > 0 ? ((totalReview - pendingResponses) * 100.0 / totalReview) : 0.0;
        return new ReviewStatsDTO(avgRating, totalReview, fiveStarReviews, fiveStarPercentage, responseRate, pendingResponses);
    }


    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getMyReviews(Long userId) {

        List<Review> reviews = reviewRepository.findByUserId(userId);

        return reviews.stream().map(review -> {

            ReviewResponseDTO dto = new ReviewResponseDTO();

            dto.setReviewId(review.getId());
            dto.setProjectName(review.getProject().getTitle());
            dto.setRating(review.getRating());
            dto.setComment(review.getComment());

            dto.setReviewerName(review.getUser().getUsername());

            // Initials (EC, FB)
            dto.setReviewerInitials(getInitials(review.getUser().getUsername()));

            // Time ago (2 days ago)
            dto.setTimeAgo(formatTimeAgo(review.getCreatedAt()));

            // Seller response (if exists)
//            if (review.getProject().getSeller() != null) {
//                dto.setSellerResponse(review.getProject().get);
//                dto.setSellerResponseTime(formatTimeAgo(
//                        review.getProject().getResponseTime()
//                ));
//            }

            return dto;

        }).toList();
    }

    private String getInitials(String name) {
        String[] parts = name.split(" ");
        return Arrays.stream(parts)
                .map(p -> p.substring(0, 1).toUpperCase())
                .reduce("", String::concat);
    }

    private String formatTimeAgo(LocalDateTime time) {
        Duration duration = Duration.between(time, LocalDateTime.now());

        if (duration.toDays() > 0)
            return duration.toDays() + " days ago";
        else if (duration.toHours() > 0)
            return duration.toHours() + " hours ago";
        else
            return "just now";
    }


}
