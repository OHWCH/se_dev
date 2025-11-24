package com.user.firstproject.domain.user.controller;

// com.user.firstproject.domain.user.controller;

import com.user.firstproject.domain.user.dto.AuthTokens;
import com.user.firstproject.domain.user.dto.SupabaseLoginRequestDto;
import com.user.firstproject.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // 기존 /register, /login, /refresh, /logout는 그대로

    /** Supabase + GitHub 로그인용 엔드포인트 */
    @PostMapping("/supabase-login")
    public ResponseEntity<AuthTokens> loginWithSupabase(
            @RequestBody SupabaseLoginRequestDto dto
    ) {
        return ResponseEntity.ok(authService.loginWithSupabase(dto));
    }
}


