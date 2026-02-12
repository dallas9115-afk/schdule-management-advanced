package com.example.schedulemanagementadvanced.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequestDto(
        @NotBlank(message = "유저명은 필수입니다.")
        @Size(min = 4, max = 10, message = "유저명은 4자 이상 10자 이하여야 합니다.")
        @Pattern(regexp = "^[a-z0-9]+$", message = "유저명은 소문자 알파벳과 숫자로만 구성되어야 합니다.")
        String username,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,15}$", message = "비밀번호는 알파벳, 숫자, 특수문자(!@#$%^&*)를 포함해야 합니다.")
        String password
) {
    // record는 getter, 생성자, toString을 자동으로 만들어줍니다!
}
