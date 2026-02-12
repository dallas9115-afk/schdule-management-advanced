package com.example.schedulemanagementadvanced.comment.controller;

import com.example.schedulemanagementadvanced.comment.dto.CommentRequestDto;
import com.example.schedulemanagementadvanced.comment.dto.CommentResponseDto;
import com.example.schedulemanagementadvanced.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // URL 설계를 유연하게 하기 위해 공통 경로만 설정
@RequiredArgsConstructor

public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    // URL: POST /api/schedules/{scheduleId}/comments
    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CommentResponseDto> save(
            @PathVariable Long scheduleId,
            @Valid @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("LOGIN_USER");

        CommentResponseDto responseDto = commentService.save(scheduleId, userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 댓글 목록 조회 (특정 일정의 댓글만)
    // URL: GET /api/schedules/{scheduleId}/comments
    @GetMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<List<CommentResponseDto>> findAll(@PathVariable Long scheduleId) {
        List<CommentResponseDto> responseDtoList = commentService.findAll(scheduleId);
        return ResponseEntity.ok(responseDtoList);
    }

    // 댓글 수정
    // URL: PUT /api/comments/{commentId}
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> update(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("LOGIN_USER");

        CommentResponseDto responseDto = commentService.update(commentId, userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 댓글 삭제
    // URL: DELETE /api/comments/{commentId}
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long commentId,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("LOGIN_USER");

        commentService.delete(commentId, userId);
        return ResponseEntity.noContent().build();
    }
}
