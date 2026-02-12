package com.example.schedulemanagementadvanced.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ScheduleRequestDto(@NotBlank(message = "제목은 필수입니다.")
                                 @Size(max = 10, message = "제목은 10글자 이내여야 합니다.")
                                 String title,

                                 @NotBlank(message = "내용은 필수입니다.")
                                 String content) {
}
