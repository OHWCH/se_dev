package com.user.firstproject.global.security;

//package com.user.firstproject.global.security;

import com.user.firstproject.global.security.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    /** 비밀번호 해시를 위해 사용하는 PasswordEncoder (이메일 로그인 대비) */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** JWT 필터 Bean 등록 */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    /** 시큐리티 필터 체인 전체 설정 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // 필요시 CORS 설정 추가
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 접근 가능한 엔드포인트
                        .requestMatchers(
                                "/api/auth/**",   // 이메일 회원가입/로그인 + supabase-login
                                "/api/github/**", // 사용 중이라면
                                "/health",        // 헬스 체크 등
                                "/",
                                "/index.html",
                                "/static/**"
                        ).permitAll()
                        // 나머지는 인증 필요
                        .anyRequest().authenticated()
                )
                // JWT 필터 추가
                .addFilterBefore(
                        jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}


