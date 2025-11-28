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

    // TODO: Post 엔티티와의 연관 관계 매핑
    // private Long postId; // 기존 Long 필드를 연관 관계로 대체
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id") // DB 컬럼 이름 설정
    private Post post;

    private Long userId;
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    /**
     * Comment 생성자.
     * @param post 댓글이 달릴 게시글 엔티티
     * @param userId 댓글 작성자의 GitHub ID
     * @param content 댓글 내용
     */
    public Comment(Post post, Long userId, String content) {
        this.post = post;
        this.userId = userId;
        this.content = content;
        this.deletedAt = null;
    }

    // Use Case #21: 소프트 삭제 플래그 설정
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        // @SQLDelete에 의해 DB에서는 자동으로 deleted_at와 updated_at이 업데이트되지만,
        // JPA 영속성 컨텍스트 내의 객체 상태를 업데이트하는 것이 좋습니다.
    }

    // 편의 메서드: CommentResponse 생성을 위해 postId를 반환합니다.
    public Long getPostId() {
        return this.post.getPostId();
    }
}