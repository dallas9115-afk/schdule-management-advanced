package com.example.schedulemanagementadvanced.user.controller;

import com.example.schedulemanagementadvanced.user.dto.LoginRequestDto;
import com.example.schedulemanagementadvanced.user.dto.SignupRequestDto;
import com.example.schedulemanagementadvanced.user.dto.UserResponseDto;
import com.example.schedulemanagementadvanced.user.entity.User;
import com.example.schedulemanagementadvanced.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        UserResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 로그인 (세션 생성)
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequestDto requestDto,
            HttpServletRequest request // 1. 요청 상자(Request)를 열어서
    ) {
        // 서비스에서 유저 검증 (실패 시 예외 발생)
        User user = userService.login(requestDto);

        // 2. 세션 생성 (없으면 새로 만들고, 있으면 가져온다)
        HttpSession session = request.getSession(true);

        // 3. 세션에 정보 저장
        // "LOGIN_USER" 로 유저의 ID를 저장.
        // ID만 넣어서 효율적 메모리 사용.
        session.setAttribute("LOGIN_USER", user.getId());

        return ResponseEntity.ok("로그인 성공");
        //'JSESSIONID' 쿠키가 자동으로 헤더에 포함됨
    }

    // 로그아웃 (세션 파기)
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // 세션이 있으면 가져오고, 없으면 null 반환
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate(); // 세션 삭제
        }

        return ResponseEntity.ok("로그아웃 성공");
    }
}
