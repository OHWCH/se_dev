package com.example.gitrajabi.user.domain.controller;
import org.springframework.web.servlet.view.RedirectView;

import com.example.gitrajabi.user.domain.dto.AuthTokens;
import com.example.gitrajabi.user.domain.dto.GithubAuthDto;
import com.example.gitrajabi.user.domain.service.GithubAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GithubAuthController {

    private final GithubAuthService githubAuthService;

    // 프론트에서 깃허브 로그인 버튼 누를 때 URL 요청
    @GetMapping("/authorize-url")
    public ResponseEntity<String> getAuthorizeUrl() {
        String url = githubAuthService.buildAuthorizeUrl();
        return ResponseEntity.ok(url);
    }

    // GitHub OAuth 콜백 (서버 직접 처리하는 경우)
    @GetMapping("/callback")
    public RedirectView callback( // 🌟 반환 타입을 RedirectView로 변경
                                  @RequestParam String code,
                                  @RequestParam(required = false) String state
    ) {
        GithubAuthDto dto = new GithubAuthDto();
        dto.setCode(code);
        dto.setState(state);
        AuthTokens tokens = githubAuthService.loginWithGithub(dto); // JWT 토큰 발급 완료

        // 🌟 프론트엔드 URL (브라우저 주소창에서 확인된 주소)
        // CallbackPage.jsx가 이 URL의 쿼리 파라미터를 읽어 토큰을 저장합니다.
        String frontendBaseUrl = "http://localhost:5173";

        // 🌟 토큰을 쿼리 파라미터로 담아 최종 리다이렉트 URL 생성
        String redirectUrl = frontendBaseUrl +
                "/callback?accessToken=" + tokens.getAccessToken();
        // 필요한 경우 &refreshToken=... 도 추가할 수 있습니다.

        // 🌟 RedirectView를 사용하여 HTTP 302 Found 응답과 Location 헤더를 클라이언트에 보냅니다.
        return new RedirectView(redirectUrl);
    }

    // 프론트가 이미 code -> accessToken 교환을 끝내고 accessToken만 보내는 경우
    @PostMapping("/login")
    public ResponseEntity<AuthTokens> loginWithGithub(@RequestBody GithubAuthDto dto) {
        AuthTokens tokens = githubAuthService.loginWithGithub(dto);
        return ResponseEntity.ok(tokens);
    }
}