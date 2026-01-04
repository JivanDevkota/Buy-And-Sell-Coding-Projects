package com.example.projecthub.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseId;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Project project;

    private Double paidAmount;


}

