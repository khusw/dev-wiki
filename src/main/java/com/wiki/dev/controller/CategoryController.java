package com.wiki.dev.controller;

import com.wiki.dev.dto.CategoryDto;
import com.wiki.dev.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Map<Object, Object>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<Object, Object>> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public ResponseEntity<Map<Object, Object>> createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @PatchMapping
    public ResponseEntity<Map<Object, Object>> updateCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping
    public ResponseEntity<Map<Object, Object>> deleteCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.deleteCategory(categoryDto);
    }
}
