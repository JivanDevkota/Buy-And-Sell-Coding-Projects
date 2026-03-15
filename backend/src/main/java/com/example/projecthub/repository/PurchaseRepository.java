package com.example.projecthub.repository;

import com.example.projecthub.model.Purchase;
import com.example.projecthub.model.PurchaseStatus;
import com.example.projecthub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    boolean existsByBuyerIdAndProjectIdAndStatus(Long buyerId, Long projectId, PurchaseStatus status);

    List<Purchase> findByBuyerIdOrderByPurchasedAtDesc(Long buyerId);

    //count by status
    long countByStatus(PurchaseStatus status);

    //count by date range
    long countByPurchasedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    //count complete purchase today
    long countByStatusAndPurchasedAtBetween(PurchaseStatus status, LocalDateTime startDate, LocalDateTime endDate);


    // SUM revenue for a date range
    @Query(" select coalesce(sum(p.paidAmount), 0) from Purchase p " +
            "where p.status = 'COMPLETED' AND  p.purchasedAt between :start and :end")
    Double sumRevenueByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // COUNT sales for date range
    @Query("SELECT COUNT(p) FROM Purchase p " +
            "WHERE p.status = 'COMPLETED' AND p.purchasedAt BETWEEN :start AND :end")
    long countCompletedByDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


    long countByBuyerId(Long buyerId);

    @Query("select coalesce(sum(p.paidAmount),0) from Purchase p " +
            "where p.buyer = :buyer and p.status = :status and p.refunded = false")
    double getLifetimeSpend(@Param("buyer") User buyer,
                            @Param("status") PurchaseStatus status);


    // PurchaseRepository.java
    @Query("SELECT COUNT(p) > 0 FROM Purchase p WHERE p.buyer.id = :userId AND p.project.id = :projectId")
    boolean existsByBuyerIdAndProjectId(@Param("userId") Long userId, @Param("projectId") Long projectId);
}
