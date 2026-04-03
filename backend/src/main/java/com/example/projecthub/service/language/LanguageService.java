package com.example.projecthub.service.language;

import com.example.projecthub.dto.language.LanguageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LanguageService {

    List<LanguageDTO> getAllLanguages();

    LanguageDTO createProgrammingLanguage(LanguageDTO languageDTO, MultipartFile file) throws IOException;
}
