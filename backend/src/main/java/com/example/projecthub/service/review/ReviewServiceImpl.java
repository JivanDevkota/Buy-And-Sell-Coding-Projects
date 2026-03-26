package com.example.projecthub.service.review;

import com.example.projecthub.dto.dashboard.DashboardResponse;
import com.example.projecthub.dto.review.ReviewResponseDTO;
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

    /**
     * Adds a new review for a project with comprehensive validation.
     * <p>
     * Validates:
     * - Buyer exists and is valid
     * - Project exists and is valid
     * - Buyer has purchased the project
     * - Buyer hasn't already reviewed the project
     * - Rating is within 1-5 range
     * - Updates project's average rating after review is added
     *
     * @param buyerId   the ID of the buyer submitting the review
     * @param projectId the ID of the project being reviewed
     * @param rating    the rating score (1-5)
     * @param comment   the review comment text
     * @return Review the created review entity
     * @throws RuntimeException if buyer not found
     * @throws RuntimeException if project not found
     * @throws RuntimeException if buyer hasn't purchased the project
     * @throws RuntimeException if buyer already reviewed this project
     * @throws RuntimeException if rating is outside 1-5 range
     */
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

    /**
     * Retrieves all reviews for a specific project.
     * <p>
     * Fetches reviews in database order (typically chronological).
     * Returns empty list if project has no reviews.
     *
     * @param userId the ID of the project for which to retrieve reviews
     * @return List of reviews for the project (empty list if no reviews)
     * @throws RuntimeException if project not found with given ID
     */
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
