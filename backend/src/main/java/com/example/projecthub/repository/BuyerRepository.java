package com.example.projecthub.repository;

import com.example.projecthub.dto.buyer.BuyerSummaryDTO;
import com.example.projecthub.model.Status;
import com.example.projecthub.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyerRepository extends JpaRepository<User, Long> {

    @Query("""
SELECT new com.example.projecthub.dto.buyer.BuyerSummaryDTO(
    u.id,
    u.username,
    u.email,
    u.fullName,
    u.profileImgUrl,
    u.createdAt,

    (SELECT COUNT(p1.id)
     FROM Purchase p1
     WHERE p1.buyer.id = u.id
     AND p1.status = com.example.projecthub.model.PurchaseStatus.COMPLETED),

    (SELECT COALESCE(SUM(p2.paidAmount), 0.0)
     FROM Purchase p2
     WHERE p2.buyer.id = u.id
     AND p2.status = com.example.projecthub.model.PurchaseStatus.COMPLETED),

    (SELECT COUNT(r1.id)
     FROM Review r1
     WHERE r1.user.id = u.id),

    CAST(u.status AS string)
)
FROM User u
WHERE EXISTS (
    SELECT r2 FROM u.roles r2 WHERE r2.name = 'ROLE_BUYER'
)
ORDER BY
    (SELECT COALESCE(SUM(p3.paidAmount), 0.0)
     FROM Purchase p3
     WHERE p3.buyer.id = u.id
     AND p3.status = com.example.projecthub.model.PurchaseStatus.COMPLETED) DESC
""")
    Page<BuyerSummaryDTO> findTopBuyers(Pageable pageable);


}
