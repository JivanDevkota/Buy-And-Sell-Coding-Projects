package com.example.projecthub.dto.purchase;

import lombok.Data;

@Data
public class RefundRequestDTO {
    private Long purchaseId;
    private String refundReason;
}
