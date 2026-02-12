package com.example.schedulemanagementadvanced.comment.service;

import com.example.schedulemanagementadvanced.comment.dto.CommentRequestDto;
import com.example.schedulemanagementadvanced.comment.dto.CommentResponseDto;
import com.example.schedulemanagementadvanced.comment.entity.Comment;
import com.example.schedulemanagementadvanced.comment.repository.CommentRepository;
import com.example.schedulemanagementadvanced.schedule.entity.Schedule;
import com.example.schedulemanagementadvanced.schedule.repository.ScheduleRepository;
import com.example.schedulemanagementadvanced.user.entity.User;
import com.example.schedulemanagementadvanced.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    @Transactional
    public CommentResponseDto save(Long scheduleId, Long userId, CommentRequestDto requestDto) {
        // 1. 일정 존재 여부 확인 (없으면 에러)
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다."));

        // 2. 유저 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        // 3. 댓글 저장
        Comment comment = new Comment(requestDto.content(), user, schedule);
        Comment savedComment = commentRepository.save(comment);

        return CommentResponseDto.from(savedComment);
    }

    // 댓글 전체 조회 (특정 일정에 달린 댓글만)
    public List<CommentResponseDto> findAll(Long scheduleId) {
        return commentRepository.findByScheduleId(scheduleId).stream()
                .map(CommentResponseDto::from)
                .toList();
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto update(Long id, Long userId, CommentRequestDto requestDto) {
        Comment comment = findCommentById(id);

        // 🚨 권한 체크: 댓글 작성자만 수정 가능
        if (!comment.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다.");
        }

        comment.update(requestDto.content());
        return CommentResponseDto.from(comment);
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long id, Long userId) {
        Comment comment = findCommentById(id);

        // 권한 체크
        if (!comment.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));
    }
}