package com.example.projecthub.service.review;

import com.example.projecthub.model.Project;
import com.example.projecthub.model.PurchaseStatus;
import com.example.projecthub.model.Review;
import com.example.projecthub.model.User;
import com.example.projecthub.repository.ProjectRepository;
import com.example.projecthub.repository.PurchaseRepository;
import com.example.projecthub.repository.ReviewRepository;
import com.example.projecthub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Service implementation for managing project reviews and ratings.
 * 
 * Handles review operations including:
 * - Adding new reviews with validation
 * - Verifying purchase eligibility before review
 * - Preventing duplicate reviews
 * - Rating validation (1-5 scale)
 * - Retrieving project reviews
 * - Updating project average ratings
 * 
 * @author Project Hub Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final PurchaseRepository purchaseRepository;

    /**
     * Adds a new review for a project with comprehensive validation.
     * 
     * Validates:
     * - Buyer exists and is valid
     * - Project exists and is valid
     * - Buyer has purchased the project
     * - Buyer hasn't already reviewed the project
     * - Rating is within 1-5 range
     * - Updates project's average rating after review is added
     * 
     * @param buyerId the ID of the buyer submitting the review
     * @param projectId the ID of the project being reviewed
     * @param rating the rating score (1-5)
     * @param comment the review comment text
     * @return Review the created review entity
     * @throws RuntimeException if buyer not found
     * @throws RuntimeException if project not found
     * @throws RuntimeException if buyer hasn't purchased the project
     * @throws RuntimeException if buyer already reviewed this project
     * @throws RuntimeException if rating is outside 1-5 range
     */
    @Transactional
    public Review addReview(Long buyerId, Long projectId, int rating, String comment) {
        
        // Validation: Buyer exists
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found with ID: " + buyerId));

        // Validation: Project exists
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        // Validation: Rating range check (must be done early for quick failure)
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Invalid rating: " + rating + ". Rating must be between 1 and 5.");
        }

        // Validation: Buyer has purchased the project
        boolean hasPurchased = purchaseRepository
                .existsByBuyerIdAndProjectIdAndStatus(buyerId, projectId, PurchaseStatus.COMPLETED);
        if (!hasPurchased) {
            throw new RuntimeException("You cannot review this project because you haven't purchased it yet.");
        }

        // Validation: Buyer hasn't already reviewed this project
        boolean alreadyReviewed = reviewRepository.existsByUserIdAndProjectId(buyerId, projectId);
        if (alreadyReviewed) {
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
        
        return savedReview;
    }

    /**
     * Retrieves all reviews for a specific project.
     * 
     * Fetches reviews in database order (typically chronological).
     * Returns empty list if project has no reviews.
     * 
     * @param projectId the ID of the project
     * @return List of reviews for the project (empty list if no reviews)
     * @throws RuntimeException if project not found with given ID
     */
    @Transactional(readOnly = true)
    public List<Review> getProjectReview(Long projectId) {
        // Validation: Project exists
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
        
        // Retrieve reviews for the project
        return reviewRepository.findByProject(project);
    }

}
