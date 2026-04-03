package com.example.projecthub.controller;

import com.example.projecthub.dto.category.CategoryDTO;
import com.example.projecthub.dto.language.LanguageDTO;
import com.example.projecthub.dto.project.ProjectResponse;
import com.example.projecthub.dto.project.PublicProjectDetailsResponse;
import com.example.projecthub.dto.project.PublicProjectResponse;
import com.example.projecthub.dto.user.AuthResponse;
import com.example.projecthub.dto.user.LoginRequest;
import com.example.projecthub.dto.user.RefreshTokenRequest;
import com.example.projecthub.dto.user.RegisterRequest;
import com.example.projecthub.model.Project;
import com.example.projecthub.repository.CategoryRepository;
import com.example.projecthub.service.admin.AdminService;
import com.example.projecthub.service.auth.AuthService;
import com.example.projecthub.service.language.LanguageService;
import com.example.projecthub.service.project.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class PublicController {

    private final ProjectService projectService;
    private final LanguageService languageService;
    private final AuthService authService;
    private final AdminService adminService;
    @GetMapping("/language/{languageId}/top")
    public ResponseEntity<Page<ProjectResponse>> getTopProjectByLanguage(@PathVariable Long languageId,
                                                                         @PageableDefault(size = 5, sort = "viewCount,desc") Pageable pageable) {
        Page<Project> topProjectsByLanguage = projectService.getTopProjectsByLanguage(languageId, pageable);
        Page<ProjectResponse> map = topProjectsByLanguage.
                map(ProjectResponse::fromEntity);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/languages")
    public ResponseEntity<List<LanguageDTO>> getAllProjectsByLanguage() {
        List<LanguageDTO> allLanguages = languageService.getAllLanguages();
        return ResponseEntity.ok(allLanguages);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse register = authService.registerUser(registerRequest);
            return ResponseEntity.status(HttpStatus.OK).body(register);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse login = authService.loginUser(loginRequest);
            return ResponseEntity.status(HttpStatus.OK).body(login);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?>refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try{
            AuthResponse response = authService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Refresh token failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?>getAllCategories(){
        List<CategoryDTO> allCategory = adminService.getAllCategory();
        return ResponseEntity.ok(allCategory);
    }

    @GetMapping("/projects")
    public ResponseEntity<List<PublicProjectResponse>> getAllProjects(){
        List<PublicProjectResponse> allProjects = projectService.getAllProjects();
        return ResponseEntity.ok(allProjects);
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<PublicProjectDetailsResponse> getProjectById(
            @PathVariable Long id
    ) {
        PublicProjectDetailsResponse response = projectService.getProjectDetails(id);
        return ResponseEntity.ok(response);
    }

}
