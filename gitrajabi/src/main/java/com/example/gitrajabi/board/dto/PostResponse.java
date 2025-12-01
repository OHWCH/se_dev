package com.example.gitrajabi.board.dto;

import java.time.LocalDateTime;
import com.example.gitrajabi.board.domain.Post;
// Post 응답 시 사용 (UI 출력용)
public record PostResponse(
        Long postId,
        String title,
        String content,
        Long userId, // 인증 시스템이 없으므로 ID를 노출합니다.
        int viewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getUserId(),
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}