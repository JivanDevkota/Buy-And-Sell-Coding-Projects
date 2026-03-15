package com.example.projecthub.repository;

import com.example.projecthub.model.User;
import com.example.projecthub.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Long> {

    long countByUserId(Long userId);

    boolean existsByUserIdAndProjectId(Long userId, Long productId);

    List<Wishlist>findByUserId(Long userId);

    Long user(User user);
}
