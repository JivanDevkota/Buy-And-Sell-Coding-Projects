package com.example.projecthub.dto.project;

import com.example.projecthub.model.Language;
import com.example.projecthub.model.Project;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    private String description;

    private String projectImgUrl;

    @NotNull(message = "Tags are required")
    @Size(min = 1, message = "At least one tag is required")
    private List<String> tags = new ArrayList<>();

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price cannot exceed 999999.99")
    private Double price;

    @NotNull(message = "Languages are required")
    @Size(min = 1, message = "At least one language is required")
//    @JsonProperty("languageIds")
    private List<Long> languageIds = new ArrayList<>();

    @NotNull(message = "Category is required")
    @Positive(message = "Category ID must be positive")
    private Long categoryId;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    /**
     * Converts a Project entity to ProjectDTO
     * @param project the project entity
     * @return ProjectDTO instance
     */
    public static ProjectDTO toDTO(Project project) {
        if (project == null) {
            return null;
        }

        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setProjectImgUrl(project.getProjectImgUrl());
        dto.setTags(project.getTags() != null ? new ArrayList<>(project.getTags()) : new ArrayList<>());
        dto.setPrice(project.getPrice());

        if (project.getLanguages() != null && !project.getLanguages().isEmpty()) {
            List<Long> languageIds = project.getLanguages()
                    .stream()
                    .map(Language::getId)
                    .collect(Collectors.toList());
            dto.setLanguageIds(languageIds);
        }

        if (project.getCategory() != null) {
            dto.setCategoryId(project.getCategory().getId());
        }

        if (project.getSeller() != null) {
            dto.setUserId(project.getSeller().getId());
        }

        return dto;
    }
}