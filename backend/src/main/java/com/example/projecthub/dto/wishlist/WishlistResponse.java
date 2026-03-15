package com.example.projecthub.dto.wishlist;

import com.example.projecthub.model.Language;
import com.example.projecthub.model.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Data Transfer Object for Wishlist response.
 * Contains wishlist item details including project information
 * such as name, description, price, and associated languages.
 *
 * @author Project Hub Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistResponse {

    private Long id;
    private Long userId;
    private Long projectId;
    private String projectName;
    private String productImg;
    private String projectDescription;
    private Double price;
    private List<String> languageName;
    private LocalDateTime addedAt;

    /**
     * Converts a Wishlist entity to WishlistResponse DTO.
     * Maps wishlist and project data to the response object,
     * including project languages as a list of language names.
     *
     * @param wishlist the Wishlist entity to convert
     * @return WishlistResponse DTO populated with wishlist data
     */
    public WishlistResponse toDto(Wishlist wishlist) {
        if (wishlist == null || wishlist.getProject() == null) {
            return new WishlistResponse();
        }
        
        return WishlistResponse.builder()
                .id(wishlist.getId())
                .userId(wishlist.getUser().getId())
                .projectId(wishlist.getProject().getId())
                .projectName(wishlist.getProject().getTitle())
                .productImg(wishlist.getProject().getProjectImgUrl())
                .projectDescription(wishlist.getProject().getDescription())
                .price(wishlist.getProject().getPrice())
                .languageName(wishlist.getProject().getLanguages() != null
                        ? wishlist.getProject().getLanguages().stream()
                                .map(Language::getName)
                                .toList()
                        : Collections.emptyList())
                .addedAt(wishlist.getAddedAt())
                .build();
    }
}
