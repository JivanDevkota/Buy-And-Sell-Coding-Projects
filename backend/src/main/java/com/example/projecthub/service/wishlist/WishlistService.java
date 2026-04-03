package com.example.projecthub.service.wishlist;

import com.example.projecthub.dto.wishlist.WishlistAddResponse;
import com.example.projecthub.dto.wishlist.WishlistResponse;
import com.example.projecthub.model.Wishlist;

import java.util.List;

public interface WishlistService {
    long countUserWishlistItems(Long userId);

    WishlistAddResponse addProjectToWishlist(Long userId, Long projectId);

    List<WishlistResponse> getWishlistItems(Long userId);

    String removeProjectFromWishlist(Long userId, Long projectId);
}


