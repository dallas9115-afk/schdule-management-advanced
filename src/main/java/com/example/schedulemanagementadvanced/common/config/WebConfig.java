package com.example.schedulemanagementadvanced.common.config;

import com.example.schedulemanagementadvanced.common.filter.LoginFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean // 빈으로 등록
    public FilterRegistrationBean<Filter> loginFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        // 1. 방금 만든 필터 클래스 등록
        filterRegistrationBean.setFilter(new LoginFilter());

        // 2. 순서 설정 (1번)
        filterRegistrationBean.setOrder(1);

        // 3. 필터를 적용할 URL 패턴 설정 ("/*" = 모든 요청에 대해 검사)
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
