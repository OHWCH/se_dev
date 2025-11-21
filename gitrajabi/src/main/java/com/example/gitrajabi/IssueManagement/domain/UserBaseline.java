package com.example.gitrajabi.IssueManagement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;

/**
 * (엔티티) 사용자가 가입한 시점의 GitHub 기여도를 저장합니다.
 * 추후 '현재 기여도(Current)'에서 이 값을 뺀 '순수 활동량'을 계산하는 데 사용됩니다.
 */
@Entity
@Getter
@Table(name = "user_baseline")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBaseline {

    @Id
    private Long userId; // User 엔티티의 ID와 동일

    @Column(nullable = false)
    private int CommitCount; // 가입 시점 Commit 횟수

    @Column(nullable = false)
    private int PrCount;     // 가입 시점 PR 횟수

    @Column(nullable = false)
    private int IssueCount;  // 가입 시점 Issue 횟수

    @Column(nullable = false)
    private Instant createdAt;

    @Builder
    public UserBaseline(Long userId, int CommitCount, int PrCount, int IssueCount) {
        this.userId = userId;
        this.CommitCount = CommitCount;
        this.PrCount = PrCount;
        this.IssueCount = IssueCount;
        this.createdAt = Instant.now();
    }
}