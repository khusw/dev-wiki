package com.wiki.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    @NotBlank(message = "Category name is required !")
    @Column(unique = true)
    private String name;

    @NotBlank(message = "Description is required !")
    private String description;
    private Integer postCount;
}
