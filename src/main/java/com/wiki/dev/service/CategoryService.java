package com.wiki.dev.service;

import com.wiki.dev.dto.CategoryDto;
import com.wiki.dev.entity.Category;
import com.wiki.dev.exception.CategoryNotFoundException;
import com.wiki.dev.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AuthService authService;

    @Transactional(readOnly = true)
    public List<CategoryDto> getAll() {
        return categoryRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto save(CategoryDto categoryDto) {
        Category category = categoryRepository.save(mapToCategory(categoryDto));
        categoryDto.setId(category.getId());
        return categoryDto;
    }

    @Transactional
    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found with id : " + id));
        return mapToDto(category);
    }

    private CategoryDto mapToDto(Category category) {
        return CategoryDto.builder().name(category.getName()).id(category.getId()).postCount(category.getPosts().size()).build();
    }

    private Category mapToCategory(CategoryDto categoryDto) {
        return Category.builder().name(categoryDto.getName()).description(categoryDto.getDescription()).user(authService.getCurrentUser()).createdDate(Instant.now()).build();
    }
}
