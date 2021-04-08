package com.wiki.dev.controller;

import com.wiki.dev.dto.CategoryDto;
import com.wiki.dev.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    @PostMapping
    public CategoryDto create(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }
}
