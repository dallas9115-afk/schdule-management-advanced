package com.example.schedulemanagementadvanced.comment.dto;

import com.example.schedulemanagementadvanced.comment.entity.Comment;
import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        String content,
        String username, // 작성자 이름
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getUsername(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
