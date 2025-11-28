package com.user.user_login.domain.user.controller;

import com.user.user_login.domain.user.dto.*;
import com.user.user_login.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegisterDto dto) {
        UserResponseDto response = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<AuthTokens> login(@RequestBody UserLoginDto dto) {
        AuthTokens tokens = authService.login(dto);
        return ResponseEntity.ok(tokens);
    }

    // 로그아웃 (Authorization 헤더 또는 userId 기반)
    @PostMapping("/logout/{userId}")
    public ResponseEntity<Void> logout(@PathVariable Long userId) {
        authService.logout(userId);
        return ResponseEntity.noContent().build();
    }

    // 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<AuthTokens> refresh(@RequestParam String refreshToken) {
        AuthTokens tokens = authService.refresh(refreshToken);
        return ResponseEntity.ok(tokens);
    }
}

