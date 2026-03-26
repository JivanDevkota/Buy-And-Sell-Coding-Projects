package com.example.projecthub.repository;

import com.example.projecthub.model.Status;
import com.example.projecthub.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT u.id, u.username, u.fullName, u.profileImgUrl, CAST(u.status AS string),
               COUNT(DISTINCT p.id) as projectCount,
               COALESCE(SUM(p.purchaseCount), 0) as totalSales,
               COALESCE(SUM(p.purchaseCount * p.price), 0.0) as totalRevenue,
               CASE WHEN COUNT(DISTINCT p.id) > 0 
                    THEN COALESCE(AVG(p.averageRating), 0.0) 
                    ELSE 0.0 END as avgRating,
               u.createdAt
        FROM User u
        LEFT JOIN u.uploadedProjects p
        WHERE u.roles IS NOT EMPTY
        AND EXISTS (SELECT r FROM u.roles r WHERE r.name = 'ROLE_SELLER')
        GROUP BY u.id, u.username, u.fullName, u.profileImgUrl, u.status, u.createdAt
        """)
    Page<Object[]> findSellerSummaries(Pageable pageable);

    @Query("""
    SELECT u.id, u.username, u.fullName, u.profileImgUrl, CAST(u.status AS string),
           COUNT(DISTINCT p.id) as projectCount,
           COALESCE(SUM(p.purchaseCount), 0) as totalSales,
           COALESCE(SUM(p.purchaseCount * p.price), 0.0) as totalRevenue,
           CASE WHEN COUNT(DISTINCT p.id) > 0 
                THEN COALESCE(AVG(p.averageRating), 0.0) 
                ELSE 0.0 END as avgRating,
           u.createdAt
    FROM User u
    LEFT JOIN u.uploadedProjects p
    WHERE u.roles IS NOT EMPTY
    AND EXISTS (SELECT r FROM u.roles r WHERE r.name = 'ROLE_SELLER')
    AND u.status = :status
    GROUP BY u.id, u.username, u.fullName, u.profileImgUrl, u.status, u.createdAt
""")
    Page<Object[]> findSellerSummariesByStatus(@Param("status") Status status, Pageable pageable);

}
