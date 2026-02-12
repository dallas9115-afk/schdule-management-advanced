package com.example.schedulemanagementadvanced.schedule.dto;

import com.example.schedulemanagementadvanced.schedule.entity.Schedule;
import java.time.LocalDateTime;

public record ScheduleResponseDto(Long id,
                                  String title,
                                  String content,
                                  String username, // 유저명만 반환
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt) {
    public static ScheduleResponseDto from(Schedule schedule) {
        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getUser().getUsername(), // 지연 로딩된 User에서 이름만 가져옴
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }
}
