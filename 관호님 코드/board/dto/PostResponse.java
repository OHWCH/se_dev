package com.example.gitrajabi.board.dto;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import com.example.gitrajabi.board.domain.Post;
// Post 응답 시 사용 (UI 출력용)
public record PostResponse(
        Long postId,
        String title,
        String content,
        Long userId, // 인증 시스템이 없으므로 ID를 노출합니다.
        // String userName, // ❌ 제거: 주석 처리된 부분 포함
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
                // "임시 사용자 이름", // ❌ 제거: 임시 사용자 이름 필드 제거
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}