package com.example.gitrajabi.user.domain.service;

import com.example.gitrajabi.user.domain.dto.AuthTokens;
import com.example.gitrajabi.user.security.JwtTokenProvider;
import com.example.gitrajabi.user.domain.entity.UserEntity;
import com.example.gitrajabi.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * GitHub OAuth 등으로 UserEntity가 확보된 상태에서
     * ERD의 is_admin 값에 따라 JWT 토큰 세트 발급
     */
    @Transactional
    public AuthTokens issueTokensForUser(UserEntity user) {
        String role = user.isAdmin() ? "ADMIN" : "USER";

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUserId(), role);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());

        return new AuthTokens(accessToken, refreshToken);
    }

    /**
     * 리프레시 토큰으로 새 액세스/리프레시 토큰 발급
     * - ERD 기준 user 테이블에 토큰 정보는 없음 → 완전 stateless 구조
     */
    @Transactional
    public AuthTokens refresh(String refreshToken) {

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. id=" + userId));

        if (user.isDeleted()) {
            throw new IllegalStateException("탈퇴한 계정입니다.");
        }

        String role = user.isAdmin() ? "ADMIN" : "USER";

        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getUserId(), role);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());

        return new AuthTokens(newAccessToken, newRefreshToken);
    }

    /**
     * 로그아웃
     * - ERD에 토큰 관련 테이블/필드가 없으므로 서버에서는 특별히 할 게 없다.
     * - 필요하면 추후 Refresh Token 저장소(별도 테이블) 만들고 여기서 삭제 처리하면 됨.
     */
    @Transactional
    public void logout(Long userId) {
        // 현재는 stateless JWT 구조이므로 비워둔다.
        // 클라이언트가 accessToken / refreshToken을 삭제하면 사실상 로그아웃.
    }
}
