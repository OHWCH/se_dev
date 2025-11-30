package com.example.gitrajabi.user.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
// 요청 들어올때 Authorization 헤더에서 Bearer 토큰 추출
// JwtTokenProvider.valiateToken()으로 검증
// 유효하면 getUserIdFromToken() → UserDetailsService 이용해서 사용자 조회
// SecurityContext에 Authentication 세팅 → 컨트롤러는 “로그인된 사용자”로 인식
// 헤더에 JWT가 없거나, 만료되었거나, 서명이 안맞으면 컨트롤러까지 요청 못들어오고 401 보냄
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = resolveToken(request);
        if (token != null && tokenProvider.validateToken(token)) {
            //토큰이 유효하면 Authentication 생성
            Authentication auth = tokenProvider.getAuthentication(token);
            // SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
