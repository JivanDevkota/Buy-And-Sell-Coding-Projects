package com.example.projecthub.service.wishlist;

import com.example.projecthub.dto.wishlist.WishlistResponse;
import com.example.projecthub.exception.ValidationException;
import com.example.projecthub.model.Project;
import com.example.projecthub.model.User;
import com.example.projecthub.model.Wishlist;
import com.example.projecthub.repository.ProjectRepository;
import com.example.projecthub.repository.UserRepository;
import com.example.projecthub.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Service implementation for managing user wishlists.
 * Handles wishlist operations including:
 * - Adding projects to wishlist with duplicate prevention
 * - Retrieving wishlist items with project details
 * - Counting total wishlist items for users
 * - Validation of user and project existence
 *
 * @author Project Hub Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    /**
     * Counts the total number of items in a user's wishlist.
     *
     * @param buyerId the ID of the user whose wishlist count is required
     * @return the count of wishlist items for the user
     */
    @Transactional(readOnly = true)
    public long countWishlist(Long buyerId) {
        log.debug("Counting wishlist items for user: {}", buyerId);
        return wishlistRepository.countByUserId(buyerId);
    }

    /**
     * Adds a project to a user's wishlist with comprehensive validation.
     * Validates:
     * - User exists and is valid
     * - Project exists and is valid
     * - Project is not already in user's wishlist
     *
     * @param userId the ID of the user adding to wishlist
     * @param projectId the ID of the project to add
     * @return the created Wishlist entity
     * @throws ValidationException if user not found
     * @throws ValidationException if project not found
     * @throws ValidationException if project already exists in wishlist
     */
    @Transactional
    public Wishlist addProjectToWishlist(Long userId, Long projectId) {
        log.info("Adding project {} to wishlist for user {}", projectId, userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new ValidationException("User not found with id: " + userId);
                });
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.error("Project not found with id: {}", projectId);
                    return new ValidationException("Project not found with id: " + projectId);
                });
        
        // Check for duplicate wishlist entry
        if (wishlistRepository.existsByUserIdAndProjectId(userId, projectId)) {
            log.warn("Project {} already exists in wishlist for user {}", projectId, userId);
            throw new ValidationException("Project already in wishlist for user id: " + userId);
        }
        
        // Create and save wishlist entry
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProject(project);
        
        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        log.info("Successfully added project {} to wishlist for user {}", projectId, userId);
        
        return savedWishlist;
    }

    /**
     * Retrieves all wishlist items for a specific user with project details.
     * Transforms Wishlist entities to WishlistResponse DTOs containing
     * project information including name, description, price, and languages.
     *
     * @param userId the ID of the user whose wishlist items are required
     * @return a list of WishlistResponse DTOs, or empty list if no wishlist items exist
     */
    @Transactional(readOnly = true)
    public List<WishlistResponse> getWishlistItem(Long userId) {
        log.debug("Fetching wishlist items for user: {}", userId);
        
        List<Wishlist> wishlistItems = wishlistRepository.findByUserId(userId);
        
        if (wishlistItems.isEmpty()) {
            log.debug("No wishlist items found for user: {}", userId);
            return Collections.emptyList();
        }
        
        return wishlistItems.stream()
                .map(wishlist -> new WishlistResponse().toDto(wishlist))
                .toList();
    }

}
