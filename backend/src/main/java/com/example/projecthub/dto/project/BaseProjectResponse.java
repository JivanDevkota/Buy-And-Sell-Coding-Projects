package com.example.projecthub.dto.project;

import com.example.projecthub.model.Project;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class BaseProjectResponse {
    private Long id;
    private String title;
    private String description;
    private String projectImgUrl;
    private Set<String> tags;
    private Double price;
    private List<String> languagesName;
    private String categoryName;

    public static BaseProjectResponse fromEntity(Project project) {
        BaseProjectResponse response = new BaseProjectResponse();
        response.setId(project.getId());
        response.setTitle(project.getTitle());
        response.setDescription(project.getDescription());
        response.setProjectImgUrl(project.getProjectImgUrl());
        response.setTags(project.getTags());
        response.setPrice(project.getPrice());
        response.setLanguagesName(project.getLanguages().stream()
                .map(lang -> lang.getName())
                .collect(Collectors.toList()));
        response.setCategoryName(project.getCategory().getName());
        return response;
    }
}
