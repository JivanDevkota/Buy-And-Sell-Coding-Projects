package com.example.projecthub.repository;

import com.example.projecthub.dto.dashboard.DashboardResponse;
import com.example.projecthub.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserIdAndProjectId(Long userId, Long projectId);

    List<Review> findByUserId(Long userId);


    @Query("""
        SELECT COALESCE(AVG(r.rating), 0)
        FROM Review r
        WHERE r.project.seller.id = :sellerId
    """)
    Double getAverageRatingBySeller(Long sellerId);

    @Query("""
        SELECT coalesce( AVG(r.rating),0)
        FROM Review r
        WHERE r.project.seller.id = :sellerId
        AND r.createdAt BETWEEN :start AND :end
    """)
    Double getAverageRatingBetweenBySeller(Long sellerId, LocalDateTime start, LocalDateTime end);

}
