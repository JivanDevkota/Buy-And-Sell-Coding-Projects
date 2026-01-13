package com.example.projecthub.service.language;

import com.example.projecthub.dto.language.LanguageDTO;
import com.example.projecthub.dto.language.ProgrammingLanguageResponse;
import com.example.projecthub.helper.ImageHelper;
import com.example.projecthub.model.Language;
import com.example.projecthub.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final ImageHelper imageHelper;

    public List<ProgrammingLanguageResponse> getAllLanguages() {
        List<Language> languages = languageRepository.findAll();
        return languages.stream()
                .limit(8)
                .map(language -> {
                    ProgrammingLanguageResponse response = new ProgrammingLanguageResponse();
                    response.setId(language.getId());
                    response.setName(language.getName());
                    response.setDescription(language.getDescription());
                    response.setIconUrl(language.getIconUrl());
                    return response;
                }).toList();
    }


    public LanguageDTO createProgrammingLanguage(LanguageDTO languageDTO, MultipartFile file)throws IOException {
        Language language = new Language();
        language.setId(languageDTO.getId());
        language.setName(languageDTO.getTitle());
        language.setDescription(languageDTO.getDescription());
        String img = imageHelper.uploadImage(file);
        language.setIconUrl(img);
        Language savedLanguage = languageRepository.save(language);
        return LanguageDTO.toDto(savedLanguage);
    }


}
