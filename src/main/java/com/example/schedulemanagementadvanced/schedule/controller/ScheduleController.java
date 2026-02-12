package com.example.schedulemanagementadvanced.schedule.controller;

import com.example.schedulemanagementadvanced.schedule.dto.ScheduleRequestDto;
import com.example.schedulemanagementadvanced.schedule.dto.ScheduleResponseDto;
import com.example.schedulemanagementadvanced.schedule.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor

public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정 생성
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> save(
            @Valid @RequestBody ScheduleRequestDto requestDto,
            HttpServletRequest request // 세션을 꺼내기 위해 필요
    ) {
        // 필터를 통과했으므로 세션과 LOGIN_USER는 무조건 있음이 보장됨
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("LOGIN_USER");

        ScheduleResponseDto responseDto = scheduleService.save(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 전체 조회 (페이징)
    // 예: /api/schedules?page=0&size=10
    @GetMapping
    public ResponseEntity<Page<ScheduleResponseDto>> findAll(
            @PageableDefault(size = 10) Pageable pageable // 기본값: 10개씩 조회
    ) {
        Page<ScheduleResponseDto> responseDtoList = scheduleService.findAll(pageable);
        return ResponseEntity.ok(responseDtoList);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findById(@PathVariable Long id) {
        ScheduleResponseDto responseDto = scheduleService.findById(id);
        return ResponseEntity.ok(responseDto);
    }

    // 일정 수정
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleRequestDto requestDto,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("LOGIN_USER");

        ScheduleResponseDto responseDto = scheduleService.update(id, userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("LOGIN_USER");

        scheduleService.delete(id, userId);
        return ResponseEntity.noContent().build(); // 204 No Content (삭제 성공 시 본문 없음)
    }
}
