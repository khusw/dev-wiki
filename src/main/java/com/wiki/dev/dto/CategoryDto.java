package com.wiki.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long categoryId;
    private String name;
    private String description;
    private Integer postCount;
}
