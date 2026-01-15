package com.example.projecthub.dto.category;

import com.example.projecthub.model.Category;
import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;

    public static CategoryDTO from(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = category.getId();
        dto.name = category.getName();
        dto.description = category.getDescription();
        return dto;
    }
}
