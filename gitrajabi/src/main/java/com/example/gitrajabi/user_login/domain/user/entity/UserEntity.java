package com.example.gitrajabi.user_login.domain.user.entity;


import com.example.gitrajabi.user_login.common.security.Role;
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
}

