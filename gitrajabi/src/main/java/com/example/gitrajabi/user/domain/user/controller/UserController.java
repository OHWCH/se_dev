package com.user.firstproject.domain.user.controller;

//package com.user.firstproject.domain.user.controller;

import com.user.firstproject.domain.user.dto.UserResponseDto;
import com.user.firstproject.domain.user.dto.UserUpdateDto;
import com.user.firstproject.domain.user.service.UserService;
import com.user.firstproject.global.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 프로필 조회/수정/탈퇴 등을 처리하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /** 내 프로필 조회 */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        UserResponseDto profile = userService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    /** 내 프로필 수정 */
    @PutMapping("/me")
    public ResponseEntity<Void> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserUpdateDto dto
    ) {
        Long userId = userDetails.getUserId();
        userService.updateProfile(userId, dto);
        return ResponseEntity.ok().build();
    }

    /** 계정 탈퇴 (소프트 삭제) */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        userService.deleteAccount(userId);
        return ResponseEntity.ok().build();
    }
}



