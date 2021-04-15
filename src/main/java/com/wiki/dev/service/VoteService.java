package com.wiki.dev.service;

import com.wiki.dev.dto.VoteDto;
import com.wiki.dev.entity.Post;
import com.wiki.dev.entity.Vote;
import com.wiki.dev.exception.DevWikiException;
import com.wiki.dev.exception.PostNotFoundException;
import com.wiki.dev.repository.PostRepository;
import com.wiki.dev.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.wiki.dev.entity.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID : " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new DevWikiException("you have already " + voteDto.getVoteType() + " for this post");
        }

        if (UPVOTE.equals(voteDto.getVoteType())) post.setVoteCount(post.getVoteCount() + 1);
        else post.setVoteCount(post.getVoteCount() - 1);

        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder().voteType(voteDto.getVoteType()).post(post).user(authService.getCurrentUser()).build();
    }
}
