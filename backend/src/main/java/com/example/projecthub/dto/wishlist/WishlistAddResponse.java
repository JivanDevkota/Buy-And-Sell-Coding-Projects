package com.example.projecthub.dto.wishlist;

import com.example.projecthub.model.Project;
import com.example.projecthub.model.Wishlist;

import java.time.LocalDateTime;

public record WishlistAddResponse(Long id,
                                  String projectTitle,
                                  String projectImageUrl,
                                  LocalDateTime addedAt) {
    public static WishlistAddResponse from(Wishlist wishlist) {
        Project p = wishlist.getProject();
        return new WishlistAddResponse(
                wishlist.getId(),
                p.getTitle(),
                p.getProjectImgUrl(),
                wishlist.getAddedAt()
        );
    }
}
