package com.example.gitrajabi.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // GitHub에서 제공하는 고유 ID (OAuth2 Principal Name)
    @Column(unique = true, nullable = false)
    private String githubId;

    private String name; // GitHub 사용자 이름
    private String email;
    private String profileImageUrl;

    public User(String githubId, String name, String email, String profileImageUrl) {
        this.githubId = githubId;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    // OAuth 로그인 시 변경된 정보(프로필 이미지 등)를 업데이트하는 메서드
    public User update(String name, String profileImageUrl) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        return this;
    }
}