package com.example.projecthub.service.wishlist;

import com.example.projecthub.dto.wishlist.WishlistResponse;
import com.example.projecthub.model.Wishlist;

import java.util.List;

public interface WishlistService {
    long countWishlist(Long buyerId);
    Wishlist addProjectToWishlist(Long userId, Long projectId);
    List<WishlistResponse> getWishlistItem(Long userId);
}
