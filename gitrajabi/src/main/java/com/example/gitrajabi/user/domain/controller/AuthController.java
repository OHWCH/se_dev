package com.example.gitrajabi.user.domain.controller;

import com.example.gitrajabi.user.domain.dto.AuthTokens;
import com.example.gitrajabi.user.domain.dto.GithubAuthDto;
import com.example.gitrajabi.user.domain.service.AuthService;
import com.example.gitrajabi.user.domain.service.GithubAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GithubAuthService githubAuthService;
    private final AuthService authService;

    /**
     * 프론트에서 GitHub 로그인 버튼 눌렀을 때 호출할 URL
     * → GitHub 인증 페이지로 리다이렉트할 수 있는 authorize URL을 반환
     */
    @GetMapping("/github/authorize-url")
    public ResponseEntity<String> getGithubAuthorizeUrl() {
        String url = githubAuthService.buildAuthorizeUrl();
        return ResponseEntity.ok(url);
    }

    /**
     * GitHub 가 되돌려주는 콜백 엔드포인트
     * - GitHub에서 code, state를 쿼리 파라미터로 넘겨줌
     * - 이 정보를 이용해 GitHubOAuth + JWT 발급까지 수행
     * - 프론트에는 accessToken / refreshToken 반환
     */
    @GetMapping("/github/callback")
    public ResponseEntity<AuthTokens> githubCallback(
            @RequestParam("code") String code,
            @RequestParam(value = "state", required = false) String state
    ) {
        GithubAuthDto dto = new GithubAuthDto();
        dto.setCode(code);
        dto.setState(state);
        dto.setAccessToken(null); // 아직 액세스 토큰이 없으므로 null

        AuthTokens tokens = githubAuthService.loginWithGithub(dto);
        return ResponseEntity.ok(tokens);
    }

    /**
     * (SPA 등에서 사용) 프론트가 code를 body로 보내는 방식의 GitHub 로그인
     * - /github/callback 대신 이걸 써도 됨
     */
    @PostMapping("/github/login")
    public ResponseEntity<AuthTokens> loginWithGithub(@RequestBody GithubAuthDto dto) {
        AuthTokens tokens = githubAuthService.loginWithGithub(dto);
        return ResponseEntity.ok(tokens);
    }

    /**
     * 리프레시 토큰으로 액세스/리프레시 토큰 재발급
     * - body: {"refreshToken": "..."}
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthTokens> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        AuthTokens tokens = authService.refresh(refreshToken);
        return ResponseEntity.ok(tokens);
    }

    /**
     * 로그아웃
     * - 현재 구조는 완전 JWT stateless 라고 가정 → 서버 쪽 할 일 거의 없음
     * - 필요하면 Refresh Token 블랙리스트 도입 시 여기서 처리
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody(required = false) Map<String, Object> body) {
        // body로 userId를 넘긴다면 꺼내서 전달할 수 있음
        // 지금 구조상 서버에 세션/토큰을 저장하지 않으므로, 클라이언트가 토큰을 삭제하는 걸로 충분
        Long userId = null;
        if (body != null && body.get("userId") instanceof Number n) {
            userId = n.longValue();
        }
        authService.logout(userId);
        return ResponseEntity.noContent().build();
    }
}
