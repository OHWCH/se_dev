package com.user.firstproject.global.security.jwt;

//package com.user.firstproject.global.security.jwt;

import com.user.firstproject.global.security.user.CustomUserDetails;
import com.user.firstproject.global.security.user.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final CustomUserDetailsService userDetailsService;
    private final Key key;
    private final long accessTokenValidityInMs;
    private final long refreshTokenValidityInMs;

    public JwtTokenProvider(
            CustomUserDetailsService userDetailsService,
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-validity-ms}") long accessTokenValidityInMs,
            @Value("${jwt.refresh-token-validity-ms}") long refreshTokenValidityInMs
    ) {
        this.userDetailsService = userDetailsService;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityInMs = accessTokenValidityInMs;
        this.refreshTokenValidityInMs = refreshTokenValidityInMs;
    }

    /** Access Token 생성 (userId + role 포함) */
    public String generateAccessToken(Long userId, String role) {
        return createToken(userId, role, accessTokenValidityInMs);
    }

    /** Refresh Token 생성 (userId만 포함) */
    public String generateRefreshToken(Long userId) {
        return createToken(userId, null, refreshTokenValidityInMs);
    }

    private String createToken(Long userId, String role, long validityMs) {
        long now = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + validityMs))
                .signWith(key, SignatureAlgorithm.HS256);

        if (role != null) {
            builder.claim("role", role);
        }

        return builder.compact();
    }

    /** 토큰 유효성 검증 */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 로그 찍고 false 반환
            return false;
        }
    }

    /** 토큰에서 userId 추출 */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    /** 토큰에서 Authentication 객체 생성 */
    public Authentication getAuthentication(String token) {
        Long userId = getUserIdFromToken(token);
        CustomUserDetails userDetails =
                (CustomUserDetails) userDetailsService.loadUserById(userId);

        return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}



