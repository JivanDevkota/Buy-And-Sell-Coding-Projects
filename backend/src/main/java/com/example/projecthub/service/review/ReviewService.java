package com.example.projecthub.service.review;

import com.example.projecthub.model.Review;

import java.util.List;

/**
 * Service interface for managing project reviews and ratings.
 * 
 * Defines contract for review operations:
 * - Adding new reviews with validation
 * - Retrieving reviews for projects
 * 
 * Implementation: ReviewServiceImpl
 * 
 * @author Project Hub Team
 * @version 1.0
 */
public interface ReviewService {

    /**
     * Retrieves all reviews for a specific project.
     * 
     * @param projectId the unique identifier of the project
     * @return list of reviews for the project (empty if no reviews exist)
     * @throws RuntimeException if project not found
     */
    List<Review> getProjectReview(Long projectId);

    /**
     * Submits a new review for a project.
     * 
     * Validates:
     * - Buyer exists in system
     * - Project exists in system
     * - Buyer has completed purchase of the project
     * - Buyer hasn't already reviewed this project
     * - Rating is valid (1-5 scale)
     * 
     * @param buyerId the unique identifier of the buyer submitting review
     * @param projectId the unique identifier of the project being reviewed
     * @param rating the numerical rating (1-5)
     * @param comment the text review/comment
     * @return the created Review entity with all fields populated
     * @throws RuntimeException if validation fails
     */
    Review addReview(Long buyerId, Long projectId, int rating, String comment);
}
