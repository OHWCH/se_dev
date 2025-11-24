package com.user.firstproject.domain.user.service;

//package com.user.firstproject.domain.user.service;

import com.user.firstproject.domain.user.dto.AuthTokens;
import com.user.firstproject.domain.user.dto.SupabaseLoginRequestDto;
import com.user.firstproject.domain.user.entity.*;
import com.user.firstproject.domain.user.repository.UserRepository;
import com.user.firstproject.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    // (기존 register, login, refresh, logout 있음)

    /** Supabase + GitHub 기반 로그인/회원가입 */
    public AuthTokens loginWithSupabase(SupabaseLoginRequestDto dto) {

        // TODO: dto.getSupabaseAccessToken()으로 Supabase 토큰 검증을 추가할 수 있음

        // 1. supabaseId로 먼저 조회
        UserEntity user = userRepository.findBySupabaseId(dto.getSupabaseId())
                .orElseGet(() -> {
                    // 2. supabaseId가 없으면 githubId로 한 번 더 조회
                    if (dto.getGithubId() != null) {
                        var byGithub = userRepository.findByGithubId(dto.getGithubId());
                        if (byGithub.isPresent()) {
                            UserEntity exist = byGithub.get();
                            exist.linkSupabase(dto.getSupabaseId());
                            return userRepository.save(exist);
                        }
                    }
                    // 3. email로도 기존 유저가 있을 수 있음
                    var byEmail = userRepository.findByEmail(dto.getEmail());
                    if (byEmail.isPresent()) {
                        UserEntity exist = byEmail.get();
                        exist.linkSupabase(dto.getSupabaseId());
                        if (dto.getGithubId() != null) exist.linkGithub(dto.getGithubId());
                        if (dto.getProfileImg() != null) exist.updateProfile(
                                dto.getNickname() != null ? dto.getNickname() : exist.getNickname(),
                                dto.getProfileImg(),
                                exist.getBio()
                        );
                        return userRepository.save(exist);
                    }

                    // 4. 완전 새로운 사용자면 회원가입
                    UserEntity newUser = UserEntity.builder()
                            .supabaseId(dto.getSupabaseId())
                            .githubId(dto.getGithubId())
                            .email(dto.getEmail() != null ? dto.getEmail() : (dto.getGithubId() + "@github.local"))
                            .nickname(dto.getNickname() != null ? dto.getNickname() : dto.getGithubId())
                            .profileImg(dto.getProfileImg())
                            .bio("")
                            .role(Role.USER)
                            .build();

                    return userRepository.save(newUser);
                });

        if (user.isDeleted()) {
            throw new IllegalStateException("탈퇴한 사용자입니다.");
        }

        // 5. 우리 서비스용 JWT 발급
        String accessToken = tokenProvider.generateAccessToken(user.getId(), user.getRole().name());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId());

        return AuthTokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}



