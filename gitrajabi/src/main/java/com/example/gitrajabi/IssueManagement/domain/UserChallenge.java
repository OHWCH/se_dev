package com.example.gitrajabi.IssueManagement.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "user_challenge")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @Column(name = "achieved_at")
    private LocalDateTime achievedAt;

    @Builder
    public UserChallenge(Long userId, Challenge challenge) {
        this.userId = userId;
        this.challenge = challenge;
        this.isCompleted = false;
    }

    // 도전과제 달성 처리 메소드
    public void complete() {
        this.isCompleted = true;
        this.achievedAt = LocalDateTime.now();
    }
}