package com.example.gitrajabi.user.domain.dto;

import com.example.gitrajabi.user.domain.entity.UserEntity;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long userId;
    private String githubId;
    private boolean admin;
    private int commitCount;
    private int issueCount;
    private int prCount;
    private Instant createdAt;
    private Instant deletedAt;

    // Entity -> DTO 변환
    public static UserResponseDto from(UserEntity user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .githubId(user.getGithubId())
                .admin(user.isAdmin())
                .commitCount(user.getCommitCount())
                .issueCount(user.getIssueCount())
                .prCount(user.getPrCount())
                .createdAt(user.getCreatedAt())
                .deletedAt(user.getDeletedAt())
                .build();
    }
}
