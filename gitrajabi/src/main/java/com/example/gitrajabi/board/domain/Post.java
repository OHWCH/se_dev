package com.example.gitrajabi.board.domain;

import com.example.gitrajabi.user_login.domain.user.entity.User; // 🌟 UserEntity 임포트
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList; // 🌟 임포트
import java.util.List; // 🌟 임포트

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW(), updated_at = NOW() WHERE post_id = ?")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    // private Long userId; // 기존 필드 유지 (FK로 사용)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User author; // 🌟 추가: 작성자 엔티티 매핑

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // 🌟 추가: 댓글 목록 매핑

    private String title;
    @Lob
    private String content;
    private int viewCount = 0;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Post(Long userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.deletedAt = null;
    }

    // Use Case #15: 게시글 수정
    public void update(String title, String content) {
        if (title != null && !title.isBlank()) { this.title = title; }
        if (content != null && !content.isBlank()) { this.content = content; }
        // updatedAt은 @LastModifiedDate에 의해 자동 업데이트
    }

    // Use Case #18: 상세 조회 시 조회수 증가
    public void incrementViewCount() { // ✅ 이 메서드를 Post 엔티티에 추가합니다.
        this.viewCount++;
    }

    // Use Case #16: 소프트 삭제 플래그 설정
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}