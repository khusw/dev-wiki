package com.wiki.dev.controller;

import com.wiki.dev.dto.PostRequest;
import com.wiki.dev.dto.PostResponse;
import com.wiki.dev.entity.Post;
import com.wiki.dev.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Post createPost(@RequestBody PostRequest postRequest) {
        return postService.save(postRequest);
    }

    @GetMapping
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @GetMapping("by-category/{id}")
    public List<PostResponse> getPostsByCategory(@PathVariable Long id) {
        return postService.getPostsByCategory(id);
    }

    @GetMapping("by-user/{name}")
    public List<PostResponse> getPostsByUsername(@PathVariable String name) {
        return postService.getPostsByUsername(name);
    }

    @PatchMapping
    public PostResponse updatePostById(@RequestBody PostRequest postRequest) {
        return postService.updatePostById(postRequest);
    }
}
