package com.example.schedulemanagementadvanced.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentRequestDto(
        @NotBlank(message = "댓글 내용은 필수입니다.")
        String content
) {
}
