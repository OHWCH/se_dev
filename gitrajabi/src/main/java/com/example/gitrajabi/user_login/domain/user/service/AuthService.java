package com.user.user_login.domain.user.service;

import com.user.user_login.common.security.JwtTokenProvider;
import com.user.user_login.common.security.Role;
import com.user.user_login.domain.user.dto.*;
import com.user.user_login.domain.user.entity.UserEntity;
import com.user.user_login.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    // 회원가입
    @Transactional
    public UserResponseDto register(UserRegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        UserEntity user = UserEntity.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .bio(dto.getBio())
                .role(Role.USER)
                .build();

        UserEntity saved = userRepository.save(user);
        return UserResponseDto.from(saved);
    }

    // 로그인
    @Transactional(readOnly = true)
    public AuthTokens login(UserLoginDto dto) {
        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = tokenProvider.generateAccessToken(user.getId(), user.getRole().name());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId());

        // 실제 서비스에서는 refreshToken을 DB나 Redis에 저장해서 관리하는 걸 권장
        return new AuthTokens(accessToken, refreshToken);
    }

    // 로그아웃 (토큰 블랙리스트나 refreshToken 삭제 등을 실제로 구현하려면 별도 저장소 필요)
    @Transactional
    public void logout(Long userId) {
        // TODO: refreshToken 저장 구조를 만든 경우 해당 userId의 refreshToken을 무효화
    }

    // 토큰 재발급
    @Transactional
    public AuthTokens refresh(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }
        Long userId = tokenProvider.getUserIdFromToken(refreshToken);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String newAccessToken = tokenProvider.generateAccessToken(user.getId(), user.getRole().name());
        String newRefreshToken = tokenProvider.generateRefreshToken(user.getId());

        return new AuthTokens(newAccessToken, newRefreshToken);
    }
}

