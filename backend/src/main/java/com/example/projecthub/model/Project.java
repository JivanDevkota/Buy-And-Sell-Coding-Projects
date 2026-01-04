package com.example.projecthub.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private List<String> tags;
    private Double price;

    @OneToMany
    private List<Language>languages;    //JAVA, TYPESCRIPT, Angular
    private List<String>technologies;
    private int viewCount;
    private int downloadCount;
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "rating_id")
    private Rating rating;

}
