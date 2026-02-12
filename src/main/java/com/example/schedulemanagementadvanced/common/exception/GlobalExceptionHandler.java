package com.example.schedulemanagementadvanced.common.exception;

import com.example.schedulemanagementadvanced.common.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // 모든 컨트롤러에서 발생하는 에러를 확인

public class GlobalExceptionHandler {

    // 1. 의도적으로 발생시킨 예외 (404 Not Found, 403 Forbidden 등)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleResponseStatusException(ResponseStatusException ex) {
        // 던진 메시지와 상태 코드를 그대로 가져와서 DTO에 담기.
        ErrorResponseDto responseDto = new ErrorResponseDto(ex.getReason(), ex.getStatusCode().value());
        return new ResponseEntity<>(responseDto, ex.getStatusCode());
    }

    // 2. @Valid 검증 실패 (400 Bad Request) - 제목 누락, 이메일 형식 오류 등
    // 어느 필드가 틀렸는지 알려주는 용도
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();

        // 에러가 난 필드(field)와 원인(message)을 담음.
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        // JSON 형태로 반환
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "입력값이 올바르지 않습니다.");
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 3. 그 외 예상치 못한 모든 시스템 에러 (500 Internal Server Error)
    // DB 연결 끊김, NullPointerException 등
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralException(Exception ex) {
        // 보안상 세부적인 에러 내용은 로그에만 남기고, 클라이언트에게는 "서버 오류" 로 반환.
        ErrorResponseDto responseDto = new ErrorResponseDto("서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}