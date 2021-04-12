package com.wiki.dev.service;

import com.wiki.dev.dto.CommentsDto;
import com.wiki.dev.entity.Comment;
import com.wiki.dev.entity.NotificationEmail;
import com.wiki.dev.entity.Post;
import com.wiki.dev.entity.User;
import com.wiki.dev.exception.PostNotFoundException;
import com.wiki.dev.mapper.CommentMapper;
import com.wiki.dev.repository.CommentRepository;
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
public class CommentService {

    private static final String POST_URL = "";

    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void createComment(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId()).orElseThrow(() -> new PostNotFoundException("can't find : " + commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post. " + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    public List<CommentsDto> getCommentByPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("can't find : " + postId.toString()));

        return commentRepository.findByPost(post).stream().map(commentMapper::mapToDto).collect(Collectors.toList());
    }

    public List<CommentsDto> getCommentsByUser(String userName) {
        User user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("can't find : " + userName));

        return commentRepository.findAllByUser(user).stream().map(commentMapper::mapToDto).collect(Collectors.toList());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " commented on your post ", user.getEmail(), message));
    }
}
