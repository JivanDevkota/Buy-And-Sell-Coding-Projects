package com.example.projecthub.dto.sller;

import lombok.Data;

@Data
public class SellerListDTO {

    private Long id;
    private Long projectCount;
    private Long salesCount;
    private Double revenue;
    private Long rating;
}
