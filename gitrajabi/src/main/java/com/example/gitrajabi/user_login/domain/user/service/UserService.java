package com.example.gitrajabi.user_login.domain.user.service;



import com.example.gitrajabi.user_login.common.security.SecurityUtil;
import com.example.gitrajabi.user_login.domain.user.dto.UserResponseDto;
import com.example.gitrajabi.user_login.domain.user.dto.UserUpdateDto;
import com.example.gitrajabi.user_login.domain.user.entity.UserEntity;
import com.example.gitrajabi.user_login.domain.user.repository.UserRepository;
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
}

