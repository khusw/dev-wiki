package com.wiki.dev.repository;

import com.wiki.dev.entity.Category;
import com.wiki.dev.entity.Post;
import com.wiki.dev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByCategory(Category category);

    List<Post> findByUser(User user);
}
