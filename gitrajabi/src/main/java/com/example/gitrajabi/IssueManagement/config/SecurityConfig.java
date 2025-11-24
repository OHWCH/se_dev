package com.example.gitrajabi.IssueManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 관련 설정을 담당합니다.
 * (바운더리 - 인증/인가 설정)
 */
@Configuration
public class SecurityConfig {

    /**
     * [주석] 이 메소드의 목적 (변경 탄력성, 올바른 기능 수행):
     * Spring Security의 필터 체인을 설정하여,
     * 어떤 요청을 인증해야 하고 어떤 요청을 허용할지 정의합니다.
     *
     * [현재 (개발 단계)]
     * 1. /api/issues/** 경로: '메인화면'에서 누구나 이슈를 검색할 수 있어야 하므로,
     * 'permitAll()'로 설정하여 인증 없이도 접근을 허용합니다.
     * 2. csrf(Cross-Site Request Forgery) 보호: Postman 같은 API 테스트 툴이나
     * React 같은 SPA(Single Page Application)와 통신할 때는
     * 일반적으로 세션 기반이 아닌 토큰 기반 인증을 사용하므로 'disable()'로 설정합니다.
     *
     * [미래 (로그인 기능 완료 시)]
     * '/api/my/**' (마이페이지) 같은 경로는 'authenticated()'로 변경하여
     * 로그인한 사용자만 접근할 수 있도록 쉽게 수정할 수 있습니다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 보호 비활성화 (API 서버로 사용)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. HTTP 요청에 대한 인가(Authorization) 규칙 설정
                .authorizeHttpRequests(auth -> auth
                        // '/api/issues/'로 시작하는 모든 요청을 허용 (기능 27)
                        .requestMatchers("/api/issues/**").permitAll()

                        // (미래를 위한 예시: /api/my/.. 경로는 인증 필요)
                        // .requestMatchers("/api/my/**").authenticated()
                        .requestMatchers("/api/my/**").permitAll()

                        // 그 외 나머지 모든 요청은 일단 허용 (필요에 따라 변경)
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}