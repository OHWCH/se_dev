package com.example.gitrajabi.user.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import lombok.Getter;

@Slf4j
@Component
// JWT만 담당하는 유틸리티/서비스
// 로그인이나 Github 로그인에서 이클래스 토근을 이용해 토큰 만듦.
// 필터에서 이 클래스를 이용해 토큰을 검증/파싱해서 userId 꺼냄
public class JwtTokenProvider {

    private final Key key;
    private final long accessTokenValidityInMs;
    private final long refreshTokenValidityInMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-validity-ms}") long accessTokenValidityInMs,
            @Value("${jwt.refresh-token-validity-ms}") long refreshTokenValidityInMs
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenValidityInMs = accessTokenValidityInMs;
        this.refreshTokenValidityInMs = refreshTokenValidityInMs;
    }
    // Access Token 생성
    public String generateAccessToken(Long userId, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidityInMs);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    // Refresh Token 생성
    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenValidityInMs);

        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // 여기서!
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    // 토큰 유효성 검증(서명, 만료 등)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
    // 토큰에서 userId 추출
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }
    //Spring Security용 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        Long userId = getUserIdFromToken(token);
        // 여기서는 JWT에서 userId만 꺼내고, SecurityContext에는 username(email) 없이 principal로 userId만 넣기도 함
        // 간단하게 Anonymous UserDetails를 만들어 사용해도 되고,
        // userId로 DB를 다시 조회해서 CustomUserDetails를 만들어도 됨.
        // 여기서는 userId 정보만 사용하는 예시:
        CustomJwtUserPrincipal principal = new CustomJwtUserPrincipal(userId);
        return new UsernamePasswordAuthenticationToken(principal, token, null);
    }

    // userId만 담는 간단한 principal
    @Getter
    public static class CustomJwtUserPrincipal {
        private final Long userId;
        public CustomJwtUserPrincipal(Long userId) {
            this.userId = userId;
        }
    }
}
