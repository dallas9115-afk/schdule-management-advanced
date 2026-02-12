package com.example.schedulemanagementadvanced.schedule.repository;

import com.example.schedulemanagementadvanced.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Page<Schedule> findAllByOrderByUpdatedAtDesc(Pageable pageable);
    // pageable : 페이지 번호와 크기를 담은 객체
    // Page<Schedule> : 데이터뿐만 아니라 '전체 페이지 수', '다음 페이지 존재 여부' 등을 담은 객체
    // OrderByUpdatedAtDesc : 수정일 기준 내림차순 정렬(최신순)
}
