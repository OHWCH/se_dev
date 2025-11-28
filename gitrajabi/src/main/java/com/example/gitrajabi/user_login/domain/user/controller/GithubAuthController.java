package com.user.user_login.domain.user.controller;

import com.user.user_login.domain.user.dto.AuthTokens;
import com.user.user_login.domain.user.dto.GithubAuthDto;
import com.user.user_login.domain.user.service.GithubAuthService;
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
    public ResponseEntity<AuthTokens> callback(
            @RequestParam String code,
            @RequestParam(required = false) String state
    ) {
        GithubAuthDto dto = new GithubAuthDto();
        dto.setCode(code);
        dto.setState(state);
        AuthTokens tokens = githubAuthService.loginWithGithub(dto);
        return ResponseEntity.ok(tokens);
    }

    // 프론트가 이미 code -> accessToken 교환을 끝내고 accessToken만 보내는 경우
    @PostMapping("/login")
    public ResponseEntity<AuthTokens> loginWithGithub(@RequestBody GithubAuthDto dto) {
        AuthTokens tokens = githubAuthService.loginWithGithub(dto);
        return ResponseEntity.ok(tokens);
    }
}

