package com.example.gitrajabi.board.domain;

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
// import com.example.gitrajabi.user.domain.entity.UserEntity; // (사용되지 않음)
// import java.util.ArrayList; // (사용되지 않음)
// import java.util.List; // (사용되지 않음)

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

    // ⭐️ user_id 칼럼을 직접 매핑 (ID-Only 방식)
    @Column(name = "user_id")
    private Long userId;

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity author;
    */ // ❌ 연관 관계 매핑 제거 (ID-Only 방식 채택)

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
    }

    // ✅ 추가: Use Case #18 - 조회수 1 증가 메서드
    public void incrementViewCount() {
        this.viewCount++;
    }

    // Use Case #16: 소프트 삭제 플래그 설정
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    // ✅ 추가: 소유권 확인 메서드 (컨트롤러/서비스에서 사용)
    public boolean isOwnedBy(Long userId) {
        return this.userId != null && this.userId.equals(userId);
    }
}