package com.example.gitrajabi.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "users") // ERD의 user 테이블 이름에 맞게
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // ERD: int user_id PK
    private Long userId;

    @Column(name = "github_id", nullable = false, unique = true, length = 255)
    private String githubId;   // ERD: string github_id

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;     // ERD: boolean is_admin

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt; // ERD: datetime created_at

    @Column(name = "deleted_at")
    private Instant deletedAt; // ERD: datetime deleted_at (nullable)

    @Column(name = "commit_count", nullable = false)
    private int commitCount;   // ERD: int commitCount

    @Column(name = "issue_count", nullable = false)
    private int issueCount;    // ERD: int IssueCount

    @Column(name = "pr_count", nullable = false)
    private int prCount;       // ERD: int PRCount

    // ========== 라이프사이클 콜백 ==========

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        // 통계 기본값 보호
        if (this.commitCount == 0) this.commitCount = 0;
        if (this.issueCount == 0) this.issueCount = 0;
        if (this.prCount == 0) this.prCount = 0;
    }

    // ========== 도메인 메서드 ==========

    /** 관리자 여부 변경 */
    public void changeAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /** 소프트 삭제 (유저 비활성화) */
    public void softDelete() {
        this.deletedAt = Instant.now();
    }

    /** 탈퇴 여부 확인 */
    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    /** 깃허브 통계 갱신 */
    public void updateStats(int commitCount, int issueCount, int prCount) {
        this.commitCount = commitCount;
        this.issueCount = issueCount;
        this.prCount = prCount;
    }
}
