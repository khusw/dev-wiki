package com.wiki.dev.controller;

import com.wiki.dev.dto.PostRequest;
import com.wiki.dev.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Map<Object, Object>> createPost(@RequestBody @Valid PostRequest postRequest) {
        return postService.createPost(postRequest);
    }

    @GetMapping
    public ResponseEntity<Map<Object, Object>> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<Object, Object>> getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @GetMapping("by-category/{id}")
    public ResponseEntity<Map<Object, Object>> getPostsByCategory(@PathVariable Long id) {
        return postService.getPostsByCategory(id);
    }

    @GetMapping("by-user/{name}")
    public ResponseEntity<Map<Object, Object>> getPostsByUsername(@PathVariable String name) {
        return postService.getPostsByUsername(name);
    }

    @PatchMapping
    public ResponseEntity<Map<Object, Object>> updatePostById(@RequestBody @Valid PostRequest postRequest) {
        return postService.updatePostById(postRequest);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<Object, Object>> deletePostById(@PathVariable Long id) {
        return postService.deletePostById(id);
    }
}
