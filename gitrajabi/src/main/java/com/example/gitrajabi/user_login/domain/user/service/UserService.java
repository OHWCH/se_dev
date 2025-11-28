package com.user.user_login.domain.user.service;

/*
import com.user.user_login.common.security.SecurityUtil;
import com.user.user_login.domain.user.dto.UserResponseDto;
import com.user.user_login.domain.user.dto.UserUpdateDto;
import com.user.user_login.domain.user.entity.UserEntity;
import com.user.user_login.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponseDto getProfile(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return UserResponseDto.from(user);
    }

    @Transactional
    public void updateProfile(Long userId, UserUpdateDto dto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.updateProfile(dto.getNickname(), dto.getProfileImg(), dto.getBio());
    }

    @Transactional
    public void changePassword(Long userId, String newPassword) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.changePassword(passwordEncoder.encode(newPassword));
    }

    @Transactional
    public void deleteAccount(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.softDelete();
    }

    //  현재 로그인한 유저 엔티티 전체
    public UserEntity getCurrentUser() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("로그인 정보가 없습니다.");
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. id=" + userId));
    }

    //  현재 로그인한 유저의 userId
    public Long getCurrentUserId() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("로그인 정보가 없습니다.");
        }
        return userId;
    }

    //  현재 로그인한 유저의 githubId
    public String getCurrentUserGithubId() {
        UserEntity user = getCurrentUser();
        return user.getGithubId();  // null이면 GitHub 미연동 상태
    }
}*/

import com.user.user_login.common.security.SecurityUtil;
import com.user.user_login.domain.user.dto.UserResponseDto;
import com.user.user_login.domain.user.entity.UserEntity;
import com.user.user_login.domain.user.repository.UserRepository;
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


