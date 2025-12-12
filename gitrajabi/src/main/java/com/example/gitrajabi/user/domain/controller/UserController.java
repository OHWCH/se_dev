package com.example.gitrajabi.user.domain.controller;

import com.example.gitrajabi.user.domain.dto.UserResponseDto;
import com.example.gitrajabi.user.domain.service.UserService;
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
