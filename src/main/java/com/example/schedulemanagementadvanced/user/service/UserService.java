package com.example.schedulemanagementadvanced.user.service;

import com.example.schedulemanagementadvanced.common.config.PasswordEncoder;
import com.example.schedulemanagementadvanced.user.dto.LoginRequestDto;
import com.example.schedulemanagementadvanced.user.dto.SignupRequestDto;
import com.example.schedulemanagementadvanced.user.dto.UserResponseDto;
import com.example.schedulemanagementadvanced.user.entity.User;
import com.example.schedulemanagementadvanced.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입 기능
    @Transactional // 데이터 변경이 일어나므로 트랜잭션
    public UserResponseDto signup(SignupRequestDto requestDto) {

        // 1. 이메일 중복 검사
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.password());

        // 3. 유저 저장 (Entity 생성 -> DB 저장)
        User user = new User(requestDto.username(), requestDto.email(), encodedPassword);
        User savedUser = userRepository.save(user);

        // 4. Entity -> DTO 변환 후 반환
        return UserResponseDto.from(savedUser);
    }

    // 로그인 기능 (인증)
    // 로그인은 DB를 읽기만 하므로 readOnly = true
    @Transactional(readOnly = true)
    public User login(LoginRequestDto requestDto) {

        // 1. 이메일로 유저 찾기 (없으면 예외)
        User user = userRepository.findByEmailOrElseThrow(requestDto.email());

        // 2. 비밀번호 검증 (암호화된 비번과 입력받은 비번 비교)
        if (!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // 3. 인증 성공 시 유저 객체 반환
        return user;
    }

    // 유저 단건 조회 (프로필)
    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
        return UserResponseDto.from(user);
    }

    // TODO: 유저 수정, 삭제 기능도 여기에 추가될 예정
}