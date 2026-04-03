package com.example.projecthub.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String bio;
    private String profileImgUrl;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double balance=0.0;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Enumerated(EnumType.STRING)
    private Status status=Status.PENDING;

    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    private List<Project>uploadedProjects=new ArrayList<>();

    @OneToMany(mappedBy = "buyer",cascade = CascadeType.ALL)
    private List<Purchase>purchases=new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review>reviews=new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wishlist>wishlists=new ArrayList<>();

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    public boolean hasPurchased(Project project) {
        return purchases != null && purchases.stream()
                .anyMatch(p -> p.getProject() != null && 
                p.getProject().getId().equals(project.getId()) &&
                p.canDownload());
    }

    /**
     * Add balance to user account
     * @param amount amount to add (must be positive)
     * @throws IllegalArgumentException if amount is negative or zero
     */
    public void addBalance(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Balance addition amount must be positive, got: " + amount);
        }
        this.balance += amount;
    }

    /**
     * Deduct balance from user account
     * @param amount amount to deduct (must be positive)
     * @throws IllegalArgumentException if amount is negative, zero, or exceeds current balance
     */
    public void deductBalance(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Balance deduction amount must be positive, got: " + amount);
        }
        if (this.balance < amount) {
            throw new IllegalArgumentException("Insufficient balance. Current: " + this.balance + ", Required: " + amount);
        }
        this.balance -= amount;
    }

}
