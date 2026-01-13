package com.example.projecthub.dto.project;


import com.example.projecthub.dto.language.LanguageDTO;
import com.example.projecthub.model.Language;
import com.example.projecthub.model.Project;
import com.example.projecthub.model.ProjectStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProjectResponse {
    private Long id;
    private String title;
    private String description;
    private String projectImgUrl;
    private List<String> tags;
    private Double price;
    private List<LanguageDTO> languages;
    private int viewCount;
    private int downloadCount;
    private int purchaseCount;
    private boolean isActive;
    private Long sellerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double averageRating;
    private Integer totalReviews;
    private ProjectStatus status;

    public static ProjectResponse toProjectDto(Project project) {
        ProjectResponse ProjectResponse = new ProjectResponse();
        ProjectResponse.setId(project.getId());
        ProjectResponse.setTitle(project.getTitle());
        ProjectResponse.setDescription(project.getDescription());
        ProjectResponse.setProjectImgUrl(project.getProjectImgUrl());
        ProjectResponse.setTags(project.getTags());
        ProjectResponse.setPrice(project.getPrice());
//        if (project.getLanguages() != null) {
//            List<String> languagesName = project.getLanguages()
//                    .stream()
//                    .map(Language::getName)
//                    .collect(Collectors.toList());
//            ProjectResponse.setLanguages(languagesName);
//        }
//        if (project.getCategory() != null) {
//            ProjectResponse.setCategoryId(project.getCategory().getId());
//        }
//        if (project.getSeller() != null) {
//            ProjectResponse.setUserId(project.getSeller().getId());
//        }
        return ProjectResponse;
    }
}

