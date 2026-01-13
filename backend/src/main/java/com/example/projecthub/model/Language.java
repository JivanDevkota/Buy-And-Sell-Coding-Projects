package com.example.projecthub.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "languages")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String name;   //java, python
    private String description;

    private String iconUrl;   //optional icon for the language

    @ManyToMany(mappedBy = "languages")
    private List<Project>projects=new ArrayList<>();


}
