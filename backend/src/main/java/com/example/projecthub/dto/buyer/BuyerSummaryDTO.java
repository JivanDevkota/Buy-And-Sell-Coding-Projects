package com.example.projecthub.dto.buyer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BuyerSummaryDTO {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String profileImgUrl;
    private LocalDateTime createdAt;
    private Long totalPurchases;
    private Double totalSpent;
    private Long reviewCount;
    private String status;

    public BuyerSummaryDTO(
            Long id,
            String username,
            String email,
            String fullName,
            String profileImgUrl,
            LocalDateTime createdAt,
            Long totalPurchases,
            Double totalSpent,
            Long reviewCount,
            String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.profileImgUrl = profileImgUrl;
        this.createdAt = createdAt;
        this.totalPurchases = totalPurchases;
        this.totalSpent = totalSpent;
        this.reviewCount = reviewCount;
        this.status = status;
    }
}