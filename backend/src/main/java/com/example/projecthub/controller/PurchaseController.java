package com.example.projecthub.controller;

import com.example.projecthub.dto.purchase.PurchaseHistoryResponseDTO;
import com.example.projecthub.dto.purchase.PurchaseRequestDTO;
import com.example.projecthub.dto.purchase.PurchaseResponseDTO;
import com.example.projecthub.dto.review.ReviewRequest;
import com.example.projecthub.dto.wishlist.WishlistResponse;
import com.example.projecthub.model.Review;
import com.example.projecthub.model.Wishlist;
import com.example.projecthub.service.download.DownloadService;
import com.example.projecthub.service.purchase.PurchaseService;
import com.example.projecthub.service.review.ReviewService;
import com.example.projecthub.service.wishlist.WishlistService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final ReviewService reviewService;
    private final WishlistService wishlistService;
    private final DownloadService downloadService;

    @PostMapping("/{buyerId}/purchases")
    public ResponseEntity<PurchaseResponseDTO>purchaseProject(@PathVariable Long buyerId, @RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        PurchaseResponseDTO purchaseResponseDTO = purchaseService.purchaseRequest(buyerId, purchaseRequestDTO);
        return ResponseEntity.ok(purchaseResponseDTO);
    }

    @GetMapping("/{buyerId}/my-purchases")
    public ResponseEntity<List<PurchaseHistoryResponseDTO>> getMyPurchases(@PathVariable Long buyerId) {
        return ResponseEntity.ok(purchaseService.getPurchaseHistory(buyerId));
    }

    @PostMapping("/reviews/project/{projectId}/user/{buyerId}")
    public ResponseEntity<?>addReview(
            @PathVariable Long projectId,
            @PathVariable Long buyerId,
            @RequestBody ReviewRequest request)
    {
        int rating = request.getRating();
        String comment = request.getComment();
        Review review = reviewService.addReview(buyerId, projectId, rating, comment);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/reviews/project/{projectId}")
    public ResponseEntity<?>getProjectReview(@PathVariable Long projectId){
        List<Review> projectReview = reviewService.getProjectReview(projectId);
        return ResponseEntity.ok(projectReview);
    }

// Buyer dashboard stats
    @GetMapping("/{buyerId}/stats")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable Long buyerId) {
        double lifetimeSpend = purchaseService.getLifeTimeSpend(buyerId);
        long purchasedCount = purchaseService.purchasedCount(buyerId);
        long wishlistCount = wishlistService.countWishlist(buyerId);

        Map<String, Object> stats = Map.of(
                "lifetimeSpend", lifetimeSpend,
                "purchasedCount", purchasedCount,
                "wishlistCount", wishlistCount
        );

        return ResponseEntity.ok(stats);
    }

    @PostMapping("/wishlist/add/{buyerId}/project/{projectId}")
    public ResponseEntity<?>addToWishlist(@PathVariable long buyerId,@PathVariable long projectId){
        Wishlist wishlist = wishlistService.addProjectToWishlist(buyerId, projectId);
        return ResponseEntity.ok(wishlist);
    }

    @GetMapping("/wishlist/{buyerId}/all")
    public ResponseEntity<?>getAllMyWishlistProjects(@PathVariable long buyerId){
        List<WishlistResponse> wishlistItem = wishlistService.getWishlistItem(buyerId);
        return ResponseEntity.ok(wishlistItem);
    }

    /**
     * GET /api/download/file/{fileId}?userId={userId}
     * Downloads a single project file.
     */
    @GetMapping("/download/file/{fileId}")
    public ResponseEntity<Resource> downloadSingleFile(
            @PathVariable Long fileId,
            @RequestParam Long userId
    ) throws IOException {

        Resource resource = downloadService.downloadProjectFile(fileId, userId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * GET /api/download/project/{projectId}?userId={userId}
     * Bundles ALL project files into a ZIP and streams it.
     */
    @GetMapping("/download/project/{projectId}")
    public ResponseEntity<StreamingResponseBody> downloadAllFiles(
            @PathVariable Long projectId,
            @RequestParam Long userId,
            HttpServletResponse response
    ) throws IOException {

        StreamingResponseBody stream = downloadService.downloadAllProjectFiles(projectId, userId, response);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/zip"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"project-" + projectId + ".zip\"")
                .body(stream);
    }
}
