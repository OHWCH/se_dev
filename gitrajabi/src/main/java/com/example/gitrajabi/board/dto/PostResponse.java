package com.example.gitrajabi.board.dto;

import com.example.gitrajabi.board.domain.Post;
import java.time.LocalDateTime;

// Post 응답 시 사용 (UI 출력용)
public record PostResponse(
        Long postId,
        String title,
        String content, // ✅ content 필드 재추가 (생성/수정 응답에 필요)
        Long userId,
        String authorGithubId, // ✅ 작성자 깃허브 아이디
        int viewCount,
        int commentCount, // ✅ 댓글 수
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    // 1. ✅ 목록 조회용 from 메서드 (깃허브 아이디, 댓글 수 포함, Content는 null/빈 문자열 처리)
    public static PostResponse from(Post post, String githubId, int commentCount) {
        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                "", // 목록 조회 시에는 content를 비웁니다.
                post.getUserId(),
                githubId,
                post.getViewCount(),
                commentCount,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    // 2. ✅ 게시글 생성/수정 응답용 from 메서드 (Content 포함, 깃허브 아이디, 댓글 수 없이 기본값 처리)
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(), // Content 포함
                post.getUserId(),
                "익명", // 임시 깃허브 아이디
                post.getViewCount(),
                0, // 댓글 수 0으로 초기화
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}