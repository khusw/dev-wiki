package com.wiki.dev.service;

import com.wiki.dev.dto.CommentsDto;
import com.wiki.dev.entity.Comment;
import com.wiki.dev.dto.NotificationEmail;
import com.wiki.dev.entity.Post;
import com.wiki.dev.entity.User;
import com.wiki.dev.mapper.CommentMapper;
import com.wiki.dev.repository.CommentRepository;
import com.wiki.dev.repository.PostRepository;
import com.wiki.dev.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {

    private static final String POST_URL = "";

    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public ResponseEntity<Map<Object, Object>> createComment(CommentsDto commentsDto) {
        Map<Object, Object> responseBody = new HashMap<>();

        Optional<Post> optionalPost = postRepository.findById(commentsDto.getPostId());

        if (optionalPost.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "post not exist ! ");
            return ResponseEntity.status(400).body(responseBody);
        }

        Post post = optionalPost.get();
        User user = (User) Objects.requireNonNull(authService.getCurrentUser().getBody()).get("data");
        Comment comment = commentMapper.mapDtoToComment(commentsDto, post, user);
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post. " + POST_URL);
        sendCommentNotification(message, post.getUser());

        responseBody.put("data", commentsDto);
        responseBody.put("error", null);

        return ResponseEntity.status(201).body(responseBody);
    }

    public ResponseEntity<Map<Object, Object>> getAllCommentByPost(Long postId) {
        Map<Object, Object> responseBody = new HashMap<>();

        Optional<Post> post = postRepository.findById(postId);

        if (post.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "post not exist !");
            return ResponseEntity.status(400).body(responseBody);
        }

        List<CommentsDto> commentsDtoList = commentRepository.findByPost(post.get()).stream().map(commentMapper::mapCommentToDto).collect(Collectors.toList());

        responseBody.put("data", commentsDtoList);
        responseBody.put("error", null);

        return ResponseEntity.status(200).body(responseBody);
    }

    public ResponseEntity<Map<Object, Object>> getAllCommentsByUser(String username) {
        Map<Object, Object> responseBody = new HashMap<>();

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "user not exist !");
            return ResponseEntity.status(400).body(responseBody);
        }

        List<CommentsDto> commentsDtoList = commentRepository.findAllByUser(user.get()).stream().map(commentMapper::mapCommentToDto).collect(Collectors.toList());

        responseBody.put("data", commentsDtoList);
        responseBody.put("error", null);

        return ResponseEntity.status(200).body(responseBody);
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " commented on your post ", user.getEmail(), message));
    }

    public ResponseEntity<Map<Object, Object>> updateComment(CommentsDto commentsDto) {
        Map<Object, Object> responseBody = new HashMap<>();

        Optional<Comment> optionalComment = commentRepository.findById(commentsDto.getCommentId());

        if (optionalComment.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "comment not exist !");
            return ResponseEntity.status(400).body(responseBody);
        }

        Comment comment = optionalComment.get();

        comment.setText(commentsDto.getText());

        commentRepository.save(comment);

        responseBody.put("data", comment);
        responseBody.put("error", null);

        return ResponseEntity.status(200).body(responseBody);
    }

    public ResponseEntity<Map<Object, Object>> deleteCommentById(Long id) {
        Map<Object, Object> responseBody = new HashMap<>();

        Optional<Comment> comment = commentRepository.findById(id);

        if (comment.isEmpty()) {
            responseBody.put("data", null);
            responseBody.put("error", "comment not exist ! ");
            return ResponseEntity.status(400).body(responseBody);
        }

        responseBody.put("data", comment.get());
        responseBody.put("error", null);

        commentRepository.delete(comment.get());

        return ResponseEntity.status(200).body(responseBody);
    }
}
