package com.example.gitrajabi.user.domain.service;

import com.example.gitrajabi.user.domain.dto.AuthTokens;
import com.example.gitrajabi.user.domain.dto.GithubAuthDto;
import com.example.gitrajabi.user.domain.entity.UserEntity;
import com.example.gitrajabi.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GithubAuthService {

    @Value("${github.client-id}")
    private String clientId;

    @Value("${github.client-secret}")
    private String clientSecret;

    @Value("${github.redirect-uri}")
    private String redirectUri;

    private final UserRepository userRepository;
    private final AuthService authService;
    private final RestTemplate restTemplate;

    /**
     * 프론트에서 GitHub 로그인 버튼을 눌렀을 때 사용할 authorize URL 생성
     */
    public String buildAuthorizeUrl() {
        return UriComponentsBuilder
                .fromHttpUrl("https://github.com/login/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", "read:user") // 필요 시 repo 권한 등 추가
                .build()
                .toUriString();
    }

    /**
     * GitHub 인가 코드(code)를 액세스 토큰으로 교환
     */
    public String exchangeCodeForAccessToken(String code) {

        String url = "https://github.com/login/oauth/access_token";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, String> body = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "code", code,
                "redirect_uri", redirectUri
        );

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalStateException("GitHub 토큰 발급에 실패했습니다.");
        }

        Object tokenValue = response.getBody().get("access_token");
        if (tokenValue == null) {
            throw new IllegalStateException("GitHub 액세스 토큰이 응답에 없습니다.");
        }

        return tokenValue.toString();
    }

    /**
     * GitHub 유저 정보 조회 (여기서는 login만 사용 → ERD github_id 로 사용)
     */
    public String fetchGithubLogin(String accessToken) {

        String url = "https://api.github.com/user";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalStateException("GitHub 프로필 조회에 실패했습니다.");
        }

        Map<String, Object> body = response.getBody();

        Object login = body.get("login");
        if (login == null) {
            throw new IllegalStateException("GitHub 응답에 login 정보가 없습니다.");
        }

        return login.toString(); // ERD github_id 로 사용
    }

    /**
     * GitHub OAuth 전체 로그인 처리
     * - GithubAuthDto: code, state, accessToken 포함 가능
     * - 1) accessToken 없으면 code로 발급
     * - 2) accessToken으로 GitHub login 조회
     * - 3) github_id 기준으로 user 조회 or 생성
     * - 4) AuthService 를 통해 JWT 토큰 발급
     */
    @Transactional
    public AuthTokens loginWithGithub(GithubAuthDto dto) {

        String accessToken = dto.getAccessToken();
        if (accessToken == null || accessToken.isBlank()) {
            accessToken = exchangeCodeForAccessToken(dto.getCode());
        }

        String githubLogin = fetchGithubLogin(accessToken); // ERD: github_id

        // ERD 기준 user 테이블에서 github_id로 유저 조회
        Optional<UserEntity> optionalUser = userRepository.findByGithubId(githubLogin);

        UserEntity user = optionalUser.orElseGet(() -> createUserFromGithubLogin(githubLogin));

        if (user.isDeleted()) {
            throw new IllegalStateException("탈퇴한 계정입니다.");
        }

        return authService.issueTokensForUser(user);
    }

    /**
     * GitHub로 처음 로그인한 사용자를 ERD에 맞게 생성
     * - github_id = GitHub login
     * - is_admin = false
     * - commit / issue / pr = 0
     * - created_at = 현재
     * - deleted_at = null
     */
    @Transactional
    protected UserEntity createUserFromGithubLogin(String githubLogin) {

        UserEntity user = UserEntity.builder()
                .githubId(githubLogin)
                .isAdmin(false)
                .commitCount(0)
                .issueCount(0)
                .prCount(0)
                .createdAt(Instant.now())
                .deletedAt(null)
                .build();

        return userRepository.save(user);
    }
}
