package com.example.gitrajabi.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "users") 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "github_id", nullable = false, unique = true, length = 255)
    private String githubId;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "commit_count", nullable = false)
    private int commitCount;

    @Column(name = "issue_count", nullable = false)
    private int issueCount;

    @Column(name = "pr_count", nullable = false)
    private int prCount;

    // 라이프사이클 콜백
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        if (this.commitCount == 0) this.commitCount = 0;
        if (this.issueCount == 0) this.issueCount = 0;
        if (this.prCount == 0) this.prCount = 0;
    }

    // 도메인 메서드들
    public void changeAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void softDelete() {
        this.deletedAt = Instant.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public void updateStats(int commitCount, int issueCount, int prCount) {
        this.commitCount = commitCount;
        this.issueCount = issueCount;
        this.prCount = prCount;
    }
}