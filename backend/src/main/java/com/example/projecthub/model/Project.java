package com.example.projecthub.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String projectImgUrl;

    @ElementCollection
    //["spring boot", "angular", "ecommerce", "payment"]
    private List<String> tags;

    private Double price;

    @ManyToMany
    @JoinTable(
            name = "project_languages",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language>languages;    //JAVA, TYPESCRIPT, Angular

    private int viewCount=0;
    private int downloadCount=0;  //purchased or
    private int purchaseCount=0;
    private boolean isActive=true;   //visable sale if true else unpublish or hidden

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;   //web dev

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Review>reviews;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL)
    private List<Purchase>purchases=new ArrayList<>();

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProjectFile>projectFiles;

    @ManyToOne
    @JoinColumn(name = "seller_id",nullable = false)
    private User seller;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Double averageRating=0.0;
    private Integer totalReviews=0;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status=ProjectStatus.DRAFT;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
    public void decreaseViewCount() {
        this.viewCount--;
    }

    public void increasePurchaseCount() {
        this.purchaseCount++;
    }

}
