package com.wiki.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDto {
    private Long commentId;
    private Long postId;
    private Instant createdDate;

    @NotEmpty(message = "comment's text is required !")
    private String text;
    private String userName;
}
