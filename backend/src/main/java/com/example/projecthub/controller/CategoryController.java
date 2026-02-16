package com.example.projecthub.controller;

import com.example.projecthub.dto.category.CategoryDTO;
import com.example.projecthub.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Slf4j
public class CategoryController {

    private final AdminService adminService;

    @PostMapping("/add/category")
    public ResponseEntity<CategoryDTO> createProduct(@RequestBody CategoryDTO dto) {
        CategoryDTO category = adminService.createCategory(dto);
        return ResponseEntity.ok().body(category);
    }

    @GetMapping("/recent/categories")
    public ResponseEntity<Map<String,Object>>getRecentAddedCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Map<String, Object> recentAddedCategoriesPaginated = adminService.getRecentAddedCategoriesPaginated(page, size);
        log.info("recentAddedCategoriesPaginated: {}", recentAddedCategoriesPaginated);
        return ResponseEntity.ok().body(recentAddedCategoriesPaginated);
    }
    


}
