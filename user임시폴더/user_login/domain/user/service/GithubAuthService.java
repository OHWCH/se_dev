package com.user.user_login.domain.user.service;

import com.user.user_login.common.security.JwtTokenProvider;
import com.user.user_login.common.security.Role;
import com.user.user_login.domain.user.dto.*;
import com.user.user_login.domain.user.entity.UserEntity;
import com.user.user_login.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GithubAuthService {

    @Value("${github.client-id}")
    private String clientId;

    @Value("${github.client-secret}")
    private String clientSecret;

    @Value("${github.redirect-uri}")
    private String redirectUri;

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final WebClient webClient = WebClient.create();

    // 깃허브 인증 URL 생성
    public String buildAuthorizeUrl() {
        String state = UUID.randomUUID().toString(); // CSRF 방지용, 실제로는 서버에 저장 후 검증 필요
        return "https://github.com/login/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&scope=user:email"
                + "&state=" + state;
    }

    // code -> accessToken 교환
    public String exchangeCodeForAccessToken(String code) {
        // GitHub OAuth 토큰 요청
        String response = webClient.post()
                .uri("https://github.com/login/oauth/access_token")
                .header("Accept", "application/json")
                .bodyValue(new GithubTokenRequest(code, clientId, clientSecret, redirectUri))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // response json 파싱해서 access_token 추출 (간단 예시)
        // {"access_token":"xxx","token_type":"bearer",...}
        // 실제로는 DTO를 만들어서 매핑하는 것이 깔끔
        String token = response.split("\"access_token\":\"")[1].split("\"")[0];
        return token;
    }

    // 깃허브 유저 정보 조회
    public GithubProfileDto fetchGithubProfile(String accessToken) {
        return webClient.get()
                .uri("https://api.github.com/user")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(GithubProfileDto.class)
                .block();
    }

    // 깃허브 로그인 처리 (최종 토큰 발급)
    public AuthTokens loginWithGithub(GithubAuthDto dto) {
        String accessToken = dto.getAccessToken();
        if (accessToken == null || accessToken.isBlank()) {
            // 프론트에서 code만 보내는 경우 서버가 교환
            accessToken = exchangeCodeForAccessToken(dto.getCode());
        }

        GithubProfileDto profile = fetchGithubProfile(accessToken);
        String githubId = String.valueOf(profile.getId());

        // githubId 기준으로 기존 유저 조회 또는 신규 생성
        UserEntity user = userRepository.findByGithubId(githubId)
                .orElseGet(() -> {
                    String email = profile.getEmail();
                    if (email == null || email.isBlank()) {
                        // 이메일이 없는 경우 github 로그인 전용 dummy 이메일 생성 등 전략 필요
                        email = profile.getLogin() + "@github.local";
                    }

                    UserEntity newUser = UserEntity.builder()
                            .email(email)
                            .password("") // 소셜 로그인 전용 계정이면 비밀번호 미사용
                            .nickname(profile.getLogin())
                            .githubId(githubId)
                            .profileImg(profile.getAvatarUrl())
                            .bio(profile.getBio())
                            .role(Role.USER)
                            .build();

                    return userRepository.save(newUser);
                });

        String jwtAccess = tokenProvider.generateAccessToken(user.getId(), user.getRole().name());
        String jwtRefresh = tokenProvider.generateRefreshToken(user.getId());

        return new AuthTokens(jwtAccess, jwtRefresh);
    }

    // 내부용 토큰 요청 DTO
    private record GithubTokenRequest(String code, String client_id, String client_secret, String redirect_uri) {}
}

