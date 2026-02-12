package com.example.schedulemanagementadvanced.user.dto;

import com.example.schedulemanagementadvanced.user.entity.User;
import java.time.LocalDateTime;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    // 💡 시니어의 팁: Entity -> DTO 변환 로직을 DTO 내부에 두면 코드가 깔끔해집니다.
    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}