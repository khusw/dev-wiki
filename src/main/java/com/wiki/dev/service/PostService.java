package com.wiki.dev.service;

import com.wiki.dev.dto.PostRequest;
import com.wiki.dev.dto.PostResponse;
import com.wiki.dev.entity.Category;
import com.wiki.dev.entity.Post;
import com.wiki.dev.entity.User;
import com.wiki.dev.mapper.PostMapper;
import com.wiki.dev.repository.CategoryRepository;
import com.wiki.dev.repository.PostRepository;
import com.wiki.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public ResponseEntity<Map<Object, Object>> getPostById(Long id) {
        Map<Object, Object> responseBody = new HashMap<>();

        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "optionalPost not exist !");
            return ResponseEntity.status(400).body(responseBody);
        }

        Post post = optionalPost.get();
        PostResponse postResponse = postMapper.mapPostToDto(post);

        responseBody.put("data", postResponse);
        responseBody.put("error", null);

        return ResponseEntity.status(200).body(responseBody);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<Object, Object>> getAllPosts() {
        Map<Object, Object> responseBody = new HashMap<>();

        List<Post> posts = postRepository.findAll();

        if (posts.isEmpty()) {
            responseBody.put("data", "post is empty, you need to add post at least one !");
            responseBody.put("error", null);
            return ResponseEntity.status(404).body(responseBody);
        }

        List<PostResponse> tempPosts = posts.stream().map(postMapper::mapPostToDto).collect(Collectors.toList());

        responseBody.put("data", tempPosts);
        responseBody.put("error", null);

        return ResponseEntity.status(200).body(responseBody);
    }

    @Transactional
    public ResponseEntity<Map<Object, Object>> createPost(PostRequest postRequest) {
        Map<Object, Object> responseBody = new HashMap<>();

        Optional<Category> category = categoryRepository.findByName(postRequest.getCategoryName());

        if (category.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "category not exist !");
            return ResponseEntity.status(400).body(responseBody);
        }

        User user = (User) Objects.requireNonNull(authService.getCurrentUser().getBody()).get("data");
        Category tempCategory = category.get();

        Post post = postRepository.save(postMapper.mapToPost(postRequest, tempCategory, user));

        responseBody.put("data", post);
        responseBody.put("error", null);

        return ResponseEntity.status(201).body(responseBody);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<Object, Object>> getPostsByCategory(Long categoryId) {
        Map<Object, Object> responseBody = new HashMap<>();

        Optional<Category> category = categoryRepository.findById(categoryId);

        if (category.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "category not exist !");
            return ResponseEntity.status(400).body(responseBody);
        }

        List<PostResponse> postResponses = postRepository.findAllByCategory(category.get())
                .stream().map(postMapper::mapPostToDto).collect(Collectors.toList());

        responseBody.put("data", postResponses);
        responseBody.put("error", null);

        return ResponseEntity.status(200).body(responseBody);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<Object, Object>> getPostsByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        Map<Object, Object> responseBody = new HashMap<>();

        if (user.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "user not exist !");
            return ResponseEntity.status(400).body(responseBody);
        }

        List<PostResponse> postResponses = postRepository.findByUser(user.get())
                .stream().map(postMapper::mapPostToDto).collect(Collectors.toList());

        responseBody.put("data", postResponses);
        responseBody.put("error", null);

        return ResponseEntity.status(200).body(responseBody);
    }

    @Transactional
    public ResponseEntity<Map<Object, Object>> updatePostById(PostRequest postRequest) {
        Map<Object, Object> responseBody = new HashMap<>();

        Optional<Post> optionalPost = postRepository.findById(postRequest.getPostId());

        if (optionalPost.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "post not exist !");
            return ResponseEntity.status(400).body(responseBody);
        }

        Post post = optionalPost.get();

        if (postRequest.getPostName() == null) {
            responseBody.put("data", null);
            responseBody.put("error", "post name cannot be null !");
            return ResponseEntity.status(400).body(responseBody);
        }

        post.setPostName(postRequest.getPostName());

        if (postRequest.getCategoryName() == null) {
            responseBody.put("data", null);
            responseBody.put("error", "you must set category for your post !");
            return ResponseEntity.status(400).body(responseBody);
        }

        if (postRequest.getUrl() != null) post.setUrl(postRequest.getUrl());

        if (postRequest.getDescription() != null) post.setDescription(postRequest.getDescription());

        User user = (User) Objects.requireNonNull(authService.getCurrentUser().getBody()).get("data");
        if (!user.getEmail().equals(post.getUser().getEmail())) {
            responseBody.put("data", null);
            responseBody.put("error", "post only can be updated by creator !");
            return ResponseEntity.status(401).body(responseBody);
        }

        post.setUser(user);

        PostResponse postResponse = postMapper.mapPostToDto(post);

        responseBody.put("data", postResponse);
        responseBody.put("error", null);

        return ResponseEntity.status(200).body(responseBody);
    }

    @Transactional
    public ResponseEntity<Map<Object, Object>> deletePostById(Long id) {
        Map<Object, Object> responseBody = new HashMap<>();

        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "post not exist !");
            return ResponseEntity.status(400).body(responseBody);
        }

        postRepository.deleteById(id);

        responseBody.put("data", optionalPost.get().getPostName() + " is deleted !");
        responseBody.put("error", null);

        return ResponseEntity.status(200).body(responseBody);
    }
}
