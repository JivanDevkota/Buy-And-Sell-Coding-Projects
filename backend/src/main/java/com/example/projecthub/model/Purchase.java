package com.example.projecthub.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private Double paidAmount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PurchaseStatus status=PurchaseStatus.PENDING;

    @OneToMany(mappedBy = "purchase",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Download>downloads=new ArrayList<>();

    private String transactionId;   //payment gateway transaction id

    private LocalDateTime purchasedAt;

    private Boolean refunded=false;
    private LocalDateTime refundedAt;
    private String refundReason;

    @PrePersist
    public void onCreate(){
        this.purchasedAt = LocalDateTime.now();
    }

    public boolean canDownload(){
        return this.status==PurchaseStatus.COMPLETED &&
                !this.refunded;
    }
}

