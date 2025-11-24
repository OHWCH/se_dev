package com.user.firstproject.domain.user.entity;

//package com.user.firstproject.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;              // 사용자 식별자(PK)

    @Column(unique = true)
    private String supabaseId;    // ✅ Supabase auth.users.id (UUID)

    @Column(nullable = false, unique = true)
    private String email;         // 로그인용 이메일(고유)

    @Column
    private String password;      // 비밀번호 해시값 (Supabase GitHub만 쓰면 null 가능)

    @Column(nullable = false)
    private String nickname;      // 닉네임

    @Column(unique = true)
    private String githubId;      // 깃허브 계정 ID

    private String profileImg;    // 프로필 이미지 URL

    @Column(length = 1000)
    private String bio;           // 자기소개

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;            // 사용자 권한 (USER/ADMIN)

    @Column(nullable = false, updatable = false)
    private Instant createdAt;    // 가입 일시

    @Column(nullable = false)
    private Instant updatedAt;    // 수정 일시

    private Instant deletedAt;    // 탈퇴(삭제) 일시

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public void updateProfile(String nickname, String profileImg, String bio) {
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.bio = bio;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
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
        return deletedAt != null;
    }

    public void linkSupabase(String supabaseId) {
        this.supabaseId = supabaseId;
    }
}



