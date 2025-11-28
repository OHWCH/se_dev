package com.user.user_login.domain.user.controller;
/*
import com.user.user_login.common.security.JwtTokenProvider;
import com.user.user_login.domain.user.dto.UserResponseDto;
import com.user.user_login.domain.user.dto.UserUpdateDto;
import com.user.user_login.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    // 토큰에서 userId 추출하는 헬퍼
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer == null || !bearer.startsWith("Bearer ")) {
            throw new IllegalArgumentException("토큰이 없습니다.");
        }
        String token = bearer.substring(7);
        return tokenProvider.getUserIdFromToken(token);
    }

    // 프로필 조회 (현재 로그인 사용자 기준)
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyProfile(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        UserResponseDto profile = userService.getProfile(userId);

        return ResponseEntity.ok(profile);
    }

    // 프로필 수정
    @PutMapping("/me")
    public ResponseEntity<Void> updateMyProfile(HttpServletRequest request,
                                                @RequestBody UserUpdateDto dto) {
        Long userId = getUserIdFromRequest(request);
        userService.updateProfile(userId, dto);
        return ResponseEntity.noContent().build();
    }

    // 회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        userService.deleteAccount(userId);
        return ResponseEntity.noContent().build();
    }
} */

import com.user.user_login.domain.user.dto.UserResponseDto;
import com.user.user_login.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 내 프로필 조회 */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyProfile() {
        UserResponseDto dto = userService.getMyProfile();
        return ResponseEntity.ok(dto);
    }

    /** github_id로 특정 사용자 정보 조회 (관리자용 예시) */
    @GetMapping("/by-github/{githubId}")
    public ResponseEntity<UserResponseDto> getByGithubId(@PathVariable String githubId) {
        UserResponseDto dto = userService.getByGithubId(githubId);
        return ResponseEntity.ok(dto);
    }


    /** 내 계정 탈퇴 (soft delete) */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount() {
        userService.deleteMyAccount();
        return ResponseEntity.noContent().build();
    }
}


