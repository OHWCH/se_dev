package com.example.gitrajabi.board.dto;

import java.time.LocalDateTime;
import com.example.gitrajabi.board.domain.Post;
// Post 응답 시 사용 (UI 출력용)
public record PostResponse(
        Long postId,
        String title,
        String content,
        Long userId, // 인증 시스템이 없으므로 ID를 노출합니다.
        //String nickname,
        int viewCount,
        // int commentCount, // 게시글 상세 조회에서 처리하거나 추후 연관 관계 매핑 시 추가
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getUserId(),
                // TODO: User 엔티티 연동 시 수정
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}