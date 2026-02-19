package com.example.projecthub.dto.sller;

import com.example.projecthub.model.Status;
import lombok.Data;

@Data
public class SellerListDTO {
    private Long sellerId;
    private String sellerName;
    private String email;
    private String profileImageUrl;
    private Integer totalProjectCount;
    private Long totalSalesCount;
    private Double totalRevenue;
    private Double averageRating;
    private Status status;
}
