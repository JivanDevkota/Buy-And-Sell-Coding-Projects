package com.example.projecthub.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PendingSellerApprovals {
    private Long id;
    private String username;
    private String email;
    private String requestedDate;
    private Long projectCount;
}
