package com.example.projecthub.controller;

import com.example.projecthub.dto.language.LanguageDTO;
import com.example.projecthub.service.language.LanguageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping("/create/language")
    public ResponseEntity<LanguageDTO>createProgrammingLanguage(@RequestPart("language") LanguageDTO languageDTO, @RequestPart("file") MultipartFile file)throws IOException {
        if (file.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty file");
        }
        LanguageDTO programmingLanguage = languageService.createProgrammingLanguage(languageDTO, file);
        return ResponseEntity.ok().body(programmingLanguage);
    }
}
