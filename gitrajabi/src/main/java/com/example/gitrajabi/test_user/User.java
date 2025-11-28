package com.example.gitrajabi.test_user;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")   // 테이블명 USER는 예약어 위험 → users 권장
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "github_id", nullable = false, length = 100, unique = true)
    private String githubId;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "commit_count", nullable = false)
    private int commitCount;

    @Column(name = "issue_count", nullable = false)
    private int issueCount;

    @Column(name = "pr_count", nullable = false)
    private int prCount;
}
