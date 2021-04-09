package com.wiki.dev.service;

import com.wiki.dev.dto.PostRequest;
import com.wiki.dev.dto.PostResponse;
import com.wiki.dev.entity.Category;
import com.wiki.dev.entity.Post;
import com.wiki.dev.entity.User;
import com.wiki.dev.exception.CategoryNotFoundException;
import com.wiki.dev.exception.PostNotFoundException;
import com.wiki.dev.mapper.PostMapper;
import com.wiki.dev.repository.CategoryRepository;
import com.wiki.dev.repository.PostRepository;
import com.wiki.dev.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("can't find : " + id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    public void save(PostRequest postRequest) {
        Category category = categoryRepository.findByName(postRequest.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException("can't find : " + postRequest.getCategoryName()));
        postRepository.save(postMapper.map(postRequest, category, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("can't find : " + categoryId.toString()));
        List<Post> posts = postRepository.findAllByCategory(category);
        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("can't find : " + username));
        return postRepository.findByUser(user).stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }
}
