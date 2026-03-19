package com.example.projecthub.service.language;

import com.example.projecthub.dto.language.LanguageDTO;
import com.example.projecthub.dto.language.ProgrammingLanguageResponse;
import com.example.projecthub.helper.ImageHelper;
import com.example.projecthub.model.Language;
import com.example.projecthub.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LanguageServiceImpl implements LanguageService {

    private static final int MAX_LANGUAGES_DISPLAY = 8;
    private static final String FILE_REQUIRED = "Language icon file is required";

    private final LanguageRepository languageRepository;
    private final ImageHelper imageHelper;

    @Override
    public List<ProgrammingLanguageResponse> getAllLanguages() {
        log.debug("Fetching all programming languages");
        return languageRepository.findAll()
                .stream()
                .limit(MAX_LANGUAGES_DISPLAY)
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public LanguageDTO createProgrammingLanguage(LanguageDTO languageDTO, MultipartFile file) throws IOException {
        log.info("Creating new programming language: {}", languageDTO.getTitle());

        if (file == null || file.isEmpty()) {
            log.error("Icon file is missing for language creation");
            throw new IllegalArgumentException(FILE_REQUIRED);
        }

        String iconUrl = imageHelper.uploadImage(file);

        Language language = new Language();
        language.setName(languageDTO.getTitle());
        language.setDescription(languageDTO.getDescription());
        language.setIconUrl(iconUrl);

        Language savedLanguage = languageRepository.save(language);
        log.info("Language created successfully with ID: {}", savedLanguage.getId());

        return LanguageDTO.toDto(savedLanguage);
    }

    private ProgrammingLanguageResponse mapToResponse(Language language) {
        ProgrammingLanguageResponse response = new ProgrammingLanguageResponse();
        response.setId(language.getId());
        response.setName(language.getName());
        response.setDescription(language.getDescription());
        response.setIconUrl(language.getIconUrl());
        return response;
    }


}
