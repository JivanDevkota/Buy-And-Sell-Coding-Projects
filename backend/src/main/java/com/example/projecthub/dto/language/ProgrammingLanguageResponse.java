package com.example.projecthub.dto.language;

import lombok.Data;

@Data
public class ProgrammingLanguageResponse {
    private Long id;
    private String name;
    private String description;
    private String iconUrl;
}
