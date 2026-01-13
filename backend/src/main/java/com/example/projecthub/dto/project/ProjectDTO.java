package com.example.projecthub.dto.project;

import com.example.projecthub.model.Language;
import com.example.projecthub.model.Project;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProjectDTO {
    private Long id;
    private String title;
    private String description;
    private String projectImgUrl;
    private List<String> tags;
    private double price;
    private List<Long>languages;
    private long categoryId;
    private long userId;


    public static ProjectDTO toDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setTitle(project.getTitle());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setProjectImgUrl(project.getProjectImgUrl());
        projectDTO.setTags(project.getTags());
        projectDTO.setPrice(project.getPrice());
        if (project.getLanguages() != null) {
            List<Long> languagesId = project.getLanguages()
                    .stream()
                    .map(Language::getId)
                    .collect(Collectors.toList());
            projectDTO.setLanguages(languagesId);
        }
        if (project.getCategory() != null) {
            projectDTO.setCategoryId(project.getCategory().getId());
        }
        if (project.getSeller() != null) {
            projectDTO.setUserId(project.getSeller().getId());
        }
        return projectDTO;
    }
}
