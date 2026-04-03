package com.example.projecthub.repository;

import com.example.projecthub.dto.seller.PendingSellerApprovals;
import com.example.projecthub.dto.seller.SellerSummaryDTO;
import com.example.projecthub.model.Status;
import com.example.projecthub.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SellerRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT new com.example.projecthub.dto.seller.SellerSummaryDTO(
            u.id,
            u.username,
            u.fullName,
            u.profileImgUrl,
            CAST(u.status AS string),

            COUNT(DISTINCT p.id),

            COALESCE(SUM(p.purchaseCount), 0),

            COALESCE(SUM(p.purchaseCount * p.price), 0.0),

            COALESCE(
                SUM(p.averageRating * p.totalReviews) / NULLIF(SUM(p.totalReviews), 0),
                0.0
            ),

            u.createdAt
        )
        FROM User u
        LEFT JOIN u.uploadedProjects p
        WHERE EXISTS (
            SELECT r FROM u.roles r WHERE r.name = 'ROLE_SELLER'
        )
        GROUP BY u.id, u.username, u.fullName, u.profileImgUrl, u.status, u.createdAt
    """)
    Page<SellerSummaryDTO> findSellerSummaries(Pageable pageable);


    @Query("""
        SELECT new com.example.projecthub.dto.seller.SellerSummaryDTO(
            u.id,
            u.username,
            u.fullName,
            u.profileImgUrl,
            CAST(u.status AS string),

            COUNT(DISTINCT p.id),

            COALESCE(SUM(p.purchaseCount), 0),

            COALESCE(SUM(p.purchaseCount * p.price), 0.0),

            COALESCE(
                SUM(p.averageRating * p.totalReviews) / NULLIF(SUM(p.totalReviews), 0),
                0.0
            ),

            u.createdAt
        )
        FROM User u
        LEFT JOIN u.uploadedProjects p
        WHERE EXISTS (
            SELECT r FROM u.roles r WHERE r.name = 'ROLE_SELLER'
        )
        AND u.status = :status
        GROUP BY u.id, u.username, u.fullName, u.profileImgUrl, u.status, u.createdAt
    """)
    Page<SellerSummaryDTO> findSellerSummariesByStatus(
            @Param("status") Status status,
            Pageable pageable
    );


    @Query("""
        SELECT COUNT(u)
        FROM User u
        WHERE EXISTS (
            SELECT r FROM u.roles r WHERE r.name = 'ROLE_SELLER'
        )
        AND u.status = :status
    """)
    Long sellerCountByStatus(@Param("status") Status status);

    @Query("""
    select new com.example.projecthub.dto.seller.PendingSellerApprovals(
        u.id,
        u.username,
        u.email,
        str(u.createdAt),
        count(p)
    )
    from User u
    left join u.uploadedProjects p
    where u.status = :status
    and exists (
        select r from u.roles r where r.name = 'ROLE_SELLER'
    )
    group by u.id, u.username, u.email, u.createdAt
""")
    List<PendingSellerApprovals> findAllPendingSellers(@Param("status") Status status);
}