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
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistServiceImpl implements WishlistService {

    private static final String USER_NOT_FOUND_MSG = "User not found with ID: ";
    private static final String PROJECT_NOT_FOUND_MSG = "Project not found with ID: ";
    private static final String PROJECT_ALREADY_IN_WISHLIST_MSG = "Project already exists in wishlist for user ID: ";

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;


    @Transactional(readOnly = true)
    public long countUserWishlistItems(Long userId) {
        log.debug("Counting wishlist items for user: {}", userId);
        return wishlistRepository.countByUserId(userId);
    }

    @Transactional
    public Wishlist addProjectToWishlist(Long userId, Long projectId) {
        log.info("Adding project {} to wishlist for user {}", projectId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new ValidationException(USER_NOT_FOUND_MSG + userId);
                });

        Project project = projectRepository.findByIdWithTags(projectId)
                .orElseThrow(() -> {
                    log.error("Project not found with id: {}", projectId);
                    return new ValidationException(PROJECT_NOT_FOUND_MSG + projectId);
                });

        if (wishlistRepository.existsByUserIdAndProjectId(userId, projectId)) {
            log.warn("Project {} already exists in wishlist for user {}", projectId, userId);
            throw new ValidationException(PROJECT_ALREADY_IN_WISHLIST_MSG + userId);
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProject(project);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        log.info("Successfully added project {} to wishlist for user {}", projectId, userId);

        return savedWishlist;
    }

    @Transactional(readOnly = true)
    public List<WishlistResponse> getWishlistItems(Long userId) {
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

    public String removeProjectFromWishlist(Long userId, Long projectId) {
        log.info("Removing project {} from wishlist for user {}", projectId, userId);

        Wishlist wishlist = wishlistRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> {
                    log.error("Wishlist item not found for user {} and project {}", userId, projectId);
                    return new ValidationException("Wishlist item not found for user ID: " + userId + " and project ID: " + projectId);
                });

        wishlistRepository.delete(wishlist);
        log.info("Successfully removed project {} from wishlist for user {}", projectId, userId);

        return "Project removed from wishlist successfully.";
    }

}
