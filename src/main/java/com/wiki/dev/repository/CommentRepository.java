package com.wiki.dev.repository;

import com.wiki.dev.entity.Comment;
import com.wiki.dev.entity.Post;
import com.wiki.dev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
