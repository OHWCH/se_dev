package com.user.user_login.domain.user.entity;
/*
import com.user.user_login.common.security.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자 식별자(PK)

    @Column(nullable = false, unique = true, length = 100)
    private String email; // 로그인용 이메일(고유)

    @Column(nullable = false)
    private String password; // 비밀번호 해시값

    @Column(nullable = false, length = 50)
    private String nickname; // 닉네임

    @Column(name = "github_id", unique = true)
    private String githubId; // 깃허브 계정 ID

    @Column(name = "profile_img")
    private String profileImg; // 프로필 이미지 URL

    @Column(length = 500)
    private String bio; // 자기소개

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role; // 사용자 권한 (USER/ADMIN)

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt; // 가입 일시

    @UpdateTimestamp
    private Instant updatedAt; // 수정 일시

    private Instant deletedAt; // 탈퇴(삭제) 일시

    // ====== Operations ======

    public void updateProfile(String nickname, String profileImg, String bio) {
        if (nickname != null) this.nickname = nickname;
        if (profileImg != null) this.profileImg = profileImg;
        if (bio != null) this.bio = bio;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void linkGithub(String githubId) {
        this.githubId = githubId;
    }

    public void unlinkGithub() {
        this.githubId = null;
    }

    public void softDelete() {
        this.deletedAt = Instant.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}*/

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

