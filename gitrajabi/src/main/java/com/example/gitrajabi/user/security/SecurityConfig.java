package com.example.gitrajabi.user.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("http://localhost:5173"); // React 개발 서버
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtTokenProvider);

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        // --- 🔥 정적 리소스 허용 ---
                        .requestMatchers(
                                "/",
                                "/favicon.ico",
                                "/error",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/static/**"
                        ).permitAll()

                        // --- 🔥 OAuth2 로그인 경로 허용 ---
                        .requestMatchers("/oauth2/**", "/login/**").permitAll()

                        // --- 🔥 Swagger 허용(선택) ---
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-resources/**"
                        ).permitAll()

                        // --- 🔥 기존 허용 경로 ---
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/github/**").permitAll()
                        .requestMatchers("/studies/**").permitAll()
                        .requestMatchers("/api/issues/good-first").permitAll()

                        // --- ✅ 게시판 조회 기능 허용 경로 추가 (GET 요청만 허용) ---
                        // GET /api/posts (목록 조회)
                        .requestMatchers(HttpMethod.GET, "/api/posts").permitAll()
                        // GET /api/posts/{postId} (상세 조회)
                        .requestMatchers(HttpMethod.GET, "/api/posts/{postId}").permitAll()

                        // --- OPTIONS 프리플라이트 허용 ---
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // --- 🔐 인증이 필요한 경로 (게시글/댓글 작성, 수정, 삭제) ---
                        // POST, PUT, DELETE /api/posts/** (게시글 CRUD)
                        .requestMatchers("/api/posts/**").authenticated()
                        // POST, DELETE /api/posts/{postId}/comments/** (댓글 작성, 삭제)
                        .requestMatchers("/api/posts/{postId}/comments/**").authenticated()

                        // --- ADMIN ---
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // --- 나머지는 인증 필요 ---
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
