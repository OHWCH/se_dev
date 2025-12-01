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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE comment SET deleted_at = NOW(), updated_at = NOW() WHERE comment_id = ?")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    // ⭐️ post_id 칼럼을 직접 매핑 (ID-Only 방식)
    @Column(name = "post_id")
    private Long postId;

    // ⭐️ user_id 칼럼을 직접 매핑 (ID-Only 방식)
    @Column(name = "user_id")
    private Long userId;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Comment(Long postId, Long userId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.deletedAt = null;
    }

    // Use Case #21: 소프트 삭제 플래그 설정
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}