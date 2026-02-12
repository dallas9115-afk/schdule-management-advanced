package com.example.schedulemanagementadvanced.schedule.service;

import com.example.schedulemanagementadvanced.schedule.dto.ScheduleRequestDto;
import com.example.schedulemanagementadvanced.schedule.dto.ScheduleResponseDto;
import com.example.schedulemanagementadvanced.schedule.entity.Schedule;
import com.example.schedulemanagementadvanced.schedule.repository.ScheduleRepository;
import com.example.schedulemanagementadvanced.user.entity.User;
import com.example.schedulemanagementadvanced.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본 읽기 전용으로 설정

public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    // 일정 생성
    @Transactional // 쓰기 작업 허용
    public ScheduleResponseDto save(Long userId, ScheduleRequestDto requestDto) {
        // 1. 작성자(User) 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        // 2. 일정 엔티티 생성 및 저장
        Schedule schedule = new Schedule(requestDto.title(), requestDto.content(), user);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        // 3. DTO 변환 반환
        return ScheduleResponseDto.from(savedSchedule);
    }

    // 전체 조회 (페이징)
    public Page<ScheduleResponseDto> findAll(Pageable pageable) {
        return scheduleRepository.findAllByOrderByUpdatedAtDesc(pageable)
                .map(ScheduleResponseDto::from); // 엔티티 리스트를 DTO 리스트로 변환
    }

    // 단건 조회
    public ScheduleResponseDto findById(Long id) {
        Schedule schedule = findScheduleById(id);
        return ScheduleResponseDto.from(schedule);
    }

    // 일정 수정 (권한 체크 포함)
    @Transactional
    public ScheduleResponseDto update(Long id, Long userId, ScheduleRequestDto requestDto) {
        Schedule schedule = findScheduleById(id);

        // 권한 체크: 작성자와 로그인한 유저가 같은가?
        if (!schedule.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다.");
        }

        // Dirty Checking: 메서드 종료 시 변경 사항이 자동 반영됨
        schedule.update(requestDto.title(), requestDto.content());

        return ScheduleResponseDto.from(schedule);
    }

    // 일정 삭제 (권한 체크 포함)
    @Transactional
    public void delete(Long id, Long userId) {
        Schedule schedule = findScheduleById(id);

        // 권한 체크
        if (!schedule.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 삭제할 수 있습니다.");
        }

        scheduleRepository.delete(schedule);
    }

    // 중복 코드를 줄이기 위한 내부 메서드
    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다."));
    }
}