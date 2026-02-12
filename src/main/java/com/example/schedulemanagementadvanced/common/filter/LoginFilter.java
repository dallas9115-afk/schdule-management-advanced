package com.example.schedulemanagementadvanced.common.filter;

import com.example.schedulemanagementadvanced.common.dto.ErrorResponseDto;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.PatternMatchUtils;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {

    // 화이트리스트: 로그인 없이도 들어갈 수 있는 "안전 구역" 정의
    private static final String[] WHITE_LIST = {"/", "/api/users/signup", "/api/users/login", "/api/users/logout"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작 {}", requestURI);

            // 1. 화이트리스트에 있는 경로인지 확인 (검사 안 함)
            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false);

                // 2. 세션이 없거나, 세션에 "LOGIN_USER" 데이터가 없으면 -> (401 error)
                if (session == null || session.getAttribute("LOGIN_USER") == null) {
                    log.info("미인증 사용자 요청 {}", requestURI);
                    // 로그인 화면으로 리다이렉트하거나, 401 에러를 보냅니다.

                    sendUnauthorizedResponse(httpResponse);
                    return; // 여기서 미인증 사용자는 컨트롤러로 넘어가지 않음
                }
            }
            // 3. 인증 통과 시, 다음 필터나 컨트롤러로 이동
            chain.doFilter(request, response);

        } catch (Exception e) {
            throw e; // 예외를 던져서 예외 처리기가 잡게 함
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }
    // 화이트리스트 검사 메서드
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }


    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        // 1. 응답 설정 (상태코드 401, 타입 JSON, 인코딩 UTF-8)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 2. ErrorResponseDto 객체 생성
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                "로그인이 필요합니다.",
                HttpServletResponse.SC_UNAUTHORIZED
        );

        // 3. Jackson 라이브러리(ObjectMapper)를 사용해 자바 객체를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = objectMapper.writeValueAsString(errorResponse);

        // 4. 응답 바디에 쓰기
        response.getWriter().write(jsonResult);
    }
}