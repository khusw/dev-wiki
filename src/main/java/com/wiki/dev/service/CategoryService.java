package com.wiki.dev.service;

import com.wiki.dev.dto.CategoryDto;
import com.wiki.dev.entity.Category;
import com.wiki.dev.mapper.CategoryMapper;
import com.wiki.dev.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public ResponseEntity<Map<Object, Object>> getAllCategories() {
        Map<Object, Object> responseBody = new HashMap<>();

        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty()) {
            responseBody.put("data", "category is empty, you need to add categories at least one");
            responseBody.put("error", null);
            return ResponseEntity.ok().body(responseBody);
        }

        List<CategoryDto> tempCategories = categories.stream().map(categoryMapper::mapCategoryToDto).collect(Collectors.toList());

        responseBody.put("data", tempCategories);
        responseBody.put("error", null);

        return ResponseEntity.ok().body(responseBody);
    }

    @Transactional
    public ResponseEntity<Map<Object, Object>> createCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.save(categoryMapper.mapDtoToCategory(categoryDto));

        Map<Object, Object> responseBody = new HashMap<>();

        responseBody.put("data", category);
        responseBody.put("error", null);

        return ResponseEntity.ok().body(responseBody);
    }

    @Transactional
    public ResponseEntity<Map<Object, Object>> getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);

        Map<Object, Object> responseBody = new HashMap<>();

        if (category.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "category not exist ! ");
            return ResponseEntity.badRequest().body(responseBody);
        }

        CategoryDto tempCategory = (CategoryDto) category.stream().map(categoryMapper::mapCategoryToDto);
        responseBody.put("data", tempCategory);
        responseBody.put("error", null);

        return ResponseEntity.ok().body(responseBody);
    }

    @Transactional
    public ResponseEntity<Map<Object, Object>> updateCategory(CategoryDto categoryDto) {
        Map<Object, Object> responseBody = new HashMap<>();

        Optional<Category> category = categoryRepository.findByName(categoryDto.getName());

        if (category.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "category not exist !");
            return ResponseEntity.badRequest().body(responseBody);
        }

        Category tempCategory = categoryMapper.mapDtoToCategory(categoryDto);

        categoryRepository.save(tempCategory);

        responseBody.put("data", tempCategory);
        responseBody.put("error", null);

        return ResponseEntity.ok().body(responseBody);
    }

    @Transactional
    public ResponseEntity<Map<Object, Object>> deleteCategory(CategoryDto categoryDto) {
        Map<Object, Object> responseBody = new HashMap<>();

        Optional<Category> category = categoryRepository.findByName(categoryDto.getName());

        if (category.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "category not exist !");
            return ResponseEntity.badRequest().body(responseBody);
        }

        Category tempCategory = category.get();

        categoryRepository.delete(tempCategory);

        responseBody.put("data", categoryDto.getName() + " is deleted !");
        responseBody.put("error", null);

        return ResponseEntity.ok().body(responseBody);
    }

}
