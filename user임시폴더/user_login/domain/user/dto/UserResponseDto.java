package com.example.gitrajabi.user_login.domain.user.dto;


import com.example.gitrajabi.user_login.common.security.Role;
import com.example.gitrajabi.user_login.domain.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
// 클라이언트로 사용자 정보를 보낼 때 사용하는 DTO
// 민감한 정보는 DTO에 포함하지 않고, 클라이언트에 필요한 정보만 UserResponseDto로 보내도록 함.
public class UserResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private boolean githubLinked;
    private String profileImg;
    private String bio;
    private Role role;
    private Instant createdAt;

    public static UserResponseDto from(UserEntity user) {
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
}

