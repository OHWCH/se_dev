package com.example.gitrajabi.user.domain.service;

import com.example.gitrajabi.user.domain.entity.UserEntity;
import com.example.gitrajabi.user.security.SecurityUtil;
import com.example.gitrajabi.user.domain.entity.UserEntity;
import com.example.gitrajabi.user.domain.dto.UserResponseDto;
import com.example.gitrajabi.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /** 현재 로그인한 사용자의 프로필(깃허브 통계 포함) 조회 */
    public UserResponseDto getMyProfile() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw new IllegalStateException("인증 정보가 없습니다. 로그인 후 이용해주세요.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. id=" + userId));

        return UserResponseDto.from(user);
    }

    /** github_id로 유저 정보 조회 (관리자/내부용) */
    public UserResponseDto getByGithubId(String githubId) {
        UserEntity user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. github_id=" + githubId));

        return UserResponseDto.from(user);
    }


    /** 현재 로그인한 사용자 탈퇴 (soft delete) */
    @Transactional
    public void deleteMyAccount() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw new IllegalStateException("인증 정보가 없습니다. 로그인 후 이용해주세요.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. id=" + userId));

        user.softDelete();
    }
}
