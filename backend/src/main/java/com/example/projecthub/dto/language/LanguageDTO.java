package com.example.projecthub.dto.language;

import com.example.projecthub.model.Language;
import lombok.Data;

@Data
public class LanguageDTO {
    private Long id;
    private String title;
    private String description;
    private String iconUrl;

    public static LanguageDTO toDto(Language language) {
        LanguageDTO dto = new LanguageDTO();
        dto.setId(language.getId());
        dto.setTitle(language.getName());
        dto.setDescription(language.getDescription());
        dto.setIconUrl(language.getIconUrl());
        return dto;
    }
}
