package com.user.firstproject.domain.user.service;

//package com.user.firstproject.domain.user.service;

import com.user.firstproject.domain.user.dto.*;
import com.user.firstproject.domain.user.entity.UserEntity;
import com.user.firstproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    public UserResponseDto getProfile(Long userId) {
        UserEntity user = findActiveUser(userId);
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .githubLinked(user.getGithubId() != null)
                .profileImg(user.getProfileImg())
                .bio(user.getBio())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }


    public void updateProfile(Long userId, UserUpdateDto dto) {
        UserEntity user = findActiveUser(userId);
        user.updateProfile(dto.getNickname(), dto.getProfileImg(), dto.getBio());
        userRepository.save(user);
    }


    public void deleteAccount(Long userId) {
        UserEntity user = findActiveUser(userId);
        user.softDelete();
        userRepository.save(user);
    }

    private UserEntity findActiveUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        if (user.isDeleted()) {
            throw new IllegalStateException("탈퇴한 사용자입니다.");
        }
        return user;
    }
}
