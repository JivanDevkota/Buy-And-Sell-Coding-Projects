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


    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    private List<Project>uploadedProjects=new ArrayList<>();

    @OneToMany(mappedBy = "buyer",cascade = CascadeType.ALL)
    private List<Purchase>purchases=new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Review>reviews=new ArrayList<>();

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
        return purchases.stream()
                .anyMatch(p->p.getProject().getId().equals(project.getId())
                && p.canDownload());
    }

    public void addBalance(double balance) {
        this.balance+=balance;
    }

    public void deductBalance(double balance) {
        this.balance-=balance;
    }

}
