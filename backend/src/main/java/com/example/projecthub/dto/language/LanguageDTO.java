package com.example.projecthub.dto.language;

import com.example.projecthub.model.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDTO {
    private Long id;
    private String name;
    private String description;
    private String iconUrl;

    public static LanguageDTO fromEntity(Language language) {
        if (language == null) {
            return null;
        }
        LanguageDTO dto = new LanguageDTO();
        dto.setId(language.getId());
        dto.setName(language.getName());
        dto.setDescription(language.getDescription());
        dto.setIconUrl(language.getIconUrl());
        return dto;
    }
}
