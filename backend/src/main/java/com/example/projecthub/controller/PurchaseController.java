package com.example.projecthub.controller;

import com.example.projecthub.dto.purchase.PurchaseHistoryResponseDTO;
import com.example.projecthub.dto.purchase.PurchaseRequestDTO;
import com.example.projecthub.dto.purchase.PurchaseResponseDTO;
import com.example.projecthub.dto.review.ReviewRequest;
import com.example.projecthub.dto.review.ReviewResponseDTO;
import com.example.projecthub.dto.wishlist.WishlistResponse;
import com.example.projecthub.model.Review;
import com.example.projecthub.model.Wishlist;
import com.example.projecthub.service.download.DownloadService;
import com.example.projecthub.service.purchase.PurchaseService;
import com.example.projecthub.service.review.ReviewService;
import com.example.projecthub.service.wishlist.WishlistService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for buyer-related operations including purchases, reviews, wishlist, and file downloads.
 * Handles all buyer endpoints for purchase management, review submission, wishlist management,
 * and project file downloads.
 *
 * Base path: /api/buyer
 */
@Slf4j
@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class PurchaseController {

    private static final String ATTACHMENT_FILENAME = "attachment; filename=\"";

    private final PurchaseService purchaseService;
    private final ReviewService reviewService;
    private final WishlistService wishlistService;
    private final DownloadService downloadService;

    @PostMapping("/{buyerId}/purchases")
    public ResponseEntity<PurchaseResponseDTO> createProjectPurchase(
            @PathVariable Long buyerId,
            @RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        log.info("Creating purchase for buyer ID: {} with project ID: {}", buyerId, purchaseRequestDTO.getProjectId());
        PurchaseResponseDTO response = purchaseService.purchaseRequest(buyerId, purchaseRequestDTO);
        log.info("Purchase created successfully for buyer ID: {}", buyerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{buyerId}/my-purchases")
    public ResponseEntity<List<PurchaseHistoryResponseDTO>> getMyPurchases(@PathVariable Long buyerId) {
        log.debug("Fetching purchase history for buyer ID: {}", buyerId);
        List<PurchaseHistoryResponseDTO> purchases = purchaseService.getPurchaseHistory(buyerId);
        log.debug("Retrieved {} purchase records for buyer ID: {}", purchases.size(), buyerId);
        return ResponseEntity.ok(purchases);
    }

    @PostMapping("/reviews/project/{projectId}/user/{buyerId}")
    public ResponseEntity<Review> submitProjectReview(
            @PathVariable Long projectId,
            @PathVariable Long buyerId,
            @RequestBody ReviewRequest request) {
        log.info("Submitting review for project ID: {} by user ID: {}", projectId, buyerId);
        int rating = request.getRating();
        String comment = request.getComment();
        Review review = reviewService.addReview(buyerId, projectId, rating, comment);
        log.info("Review submitted successfully for project ID: {}", projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @GetMapping("/reviews/project/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>> getMyProjectReviews(@PathVariable Long userId) {
        log.debug("Fetching project reviews for user ID: {}", userId);
        List<ReviewResponseDTO> myReviews = reviewService.getMyReviews(userId);
        log.debug("Retrieved {} reviews for user ID: {}", myReviews.size(), userId);
        return ResponseEntity.ok(myReviews);
    }

    @GetMapping("/{buyerId}/stats")
    public ResponseEntity<Map<String, Object>> getBuyerStatistics(@PathVariable Long buyerId) {
        log.debug("Fetching buyer statistics for user ID: {}", buyerId);

        double lifetimeSpend = purchaseService.getLifeTimeSpend(buyerId);
        long purchasedCount = purchaseService.purchasedCount(buyerId);
        long wishlistCount = wishlistService.countUserWishlistItems(buyerId);

        Map<String, Object> stats = Map.of(
                "lifetimeSpend", lifetimeSpend,
                "purchasedCount", purchasedCount,
                "wishlistCount", wishlistCount
        );

        log.debug("Retrieved statistics for buyer ID: {} - Lifetime Spend: {}, Purchased: {}, Wishlist: {}",
                buyerId, lifetimeSpend, purchasedCount, wishlistCount);

        return ResponseEntity.ok(stats);
    }

    @PostMapping("/wishlist/add/{buyerId}/project/{projectId}")
    public ResponseEntity<Wishlist> addProjectToWishlist(
            @PathVariable Long buyerId,
            @PathVariable Long projectId) {
        log.info("Adding project {} to wishlist for buyer ID: {}", projectId, buyerId);
        Wishlist wishlist = wishlistService.addProjectToWishlist(buyerId, projectId);
        log.info("Project {} successfully added to wishlist for buyer ID: {}", projectId, buyerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlist);
    }

    @GetMapping("/wishlist/{buyerId}/all")
    public ResponseEntity<List<WishlistResponse>> getAllMyWishlistProjects(@PathVariable Long buyerId) {
        log.debug("Fetching all wishlist items for buyer ID: {}", buyerId);
        List<WishlistResponse> wishlistItems = wishlistService.getWishlistItems(buyerId);
        log.debug("Retrieved {} wishlist items for buyer ID: {}", wishlistItems.size(), buyerId);
        return ResponseEntity.ok(wishlistItems);
    }

    @DeleteMapping("/{buyerId}/wishlist/{projectId}")
    public ResponseEntity<String>deleteProjectFromWishlist(
            @PathVariable Long buyerId,
            @PathVariable Long projectId) {
        log.info("Removing project {} from wishlist for buyer ID: {}", projectId, buyerId);
        wishlistService.removeProjectFromWishlist(buyerId, projectId);
        log.info("Project {} successfully removed from wishlist for buyer ID: {}", projectId, buyerId);
        return ResponseEntity.ok("Project removed from wishlist successfully.");
    }

    /**
     * Downloads a single project file.
     * GET /api/buyer/download/file/{fileId}?userId={userId}
     *
     * @param fileId the file ID to download
     * @param userId the buyer ID requesting the download
     * @return the file resource with attachment headers
     * @throws IOException if file cannot be read
     */
    @GetMapping("/download/file/{fileId}")
    public ResponseEntity<Resource> downloadSingleFile(
            @PathVariable Long fileId,
            @RequestParam Long userId) throws IOException {
        log.info("Downloading file {} requested by user {}", fileId, userId);

        Resource resource = downloadService.downloadProjectFile(fileId, userId);

        log.info("File {} downloaded successfully by user {}", fileId, userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ATTACHMENT_FILENAME + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * Downloads all project files bundled in a ZIP archive.
     * GET /api/buyer/download/project/{projectId}?userId={userId}
     *
     * @param projectId the project ID containing files to download
     * @param userId the buyer ID requesting the download
     * @param response the HTTP servlet response for streaming
     * @return a streaming response body with ZIP content
     * @throws IOException if files cannot be read or ZIP cannot be created
     */
    @GetMapping("/download/project/{projectId}")
    public ResponseEntity<StreamingResponseBody> downloadAllProjectFiles(
            @PathVariable Long projectId,
            @RequestParam Long userId,
            HttpServletResponse response) throws IOException {
        log.info("Downloading all files for project {} requested by user {}", projectId, userId);

        StreamingResponseBody stream = downloadService.downloadAllProjectFiles(projectId, userId, response);

        log.info("All files for project {} downloaded successfully by user {}", projectId, userId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/zip"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ATTACHMENT_FILENAME + "project-" + projectId + ".zip\"")
                .body(stream);
    }
}
