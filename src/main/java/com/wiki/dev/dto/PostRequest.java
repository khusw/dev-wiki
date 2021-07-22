package com.wiki.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private Long postId;

    @NotBlank(message = "Post's name cannot be empty or null")
    private String postName;

    @NotBlank(message = "Post's name cannot be empty or null")
    private String categoryName;

    private String url;
    private String description;
}
