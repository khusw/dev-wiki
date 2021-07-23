package com.wiki.dev.controller;

import com.wiki.dev.dto.CommentsDto;
import com.wiki.dev.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Map<Object, Object>> createComment(@RequestBody @Valid CommentsDto commentsDto) {
        return commentService.createComment(commentsDto);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<Map<Object, Object>> getAllCommentsByPost(@PathVariable Long postId) {
        return commentService.getAllCommentByPost(postId);
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<Map<Object, Object>> getAllCommentsByUser(@PathVariable String username) {
        return commentService.getAllCommentsByUser(username);
    }

    @PatchMapping
    public ResponseEntity<Map<Object, Object>> updateComment(@RequestBody @Valid CommentsDto commentsDto) {
        return commentService.updateComment(commentsDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<Object, Object>> deleteCommentById(@PathVariable Long id) {
        return commentService.deleteCommentById(id);
    }
}
