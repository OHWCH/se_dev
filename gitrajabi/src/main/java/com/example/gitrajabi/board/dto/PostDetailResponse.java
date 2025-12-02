package com.example.gitrajabi.board.dto;

import com.example.gitrajabi.board.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "게시글 상세 조회 응답 DTO (댓글 포함)")
public record PostDetailResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long postId,
        @Schema(description = "제목", example = "게시글 제목입니다")
        String title,
        @Schema(description = "내용", example = "게시글 내용입니다.")
        String content,
        @Schema(description = "작성자 사용자 ID", example = "100")
        Long userId, // ✅ userId만 사용
        @Schema(description = "작성자 깃허브 아이디", example = "github_user_1234") // ✅ 추가
        String authorGithubId, // ✅ 추가
        @Schema(description = "조회수", example = "10")
        int viewCount,
        @Schema(description = "생성 일시", example = "2024-01-01T10:00:00")
        LocalDateTime createdAt,
        @Schema(description = "수정 일시", example = "2024-01-01T10:30:00")
        LocalDateTime updatedAt,

        @Schema(description = "댓글 목록")
        List<CommentResponse> comments // 🌟 상세 조회용: 댓글 리스트 추가
) {
    // ✅ 수정: 깃허브 아이디를 인자로 받도록 변경
    public static PostDetailResponse from(Post post, String authorGithubId, List<CommentResponse> commentResponses) {
        return new PostDetailResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getUserId(),
                authorGithubId, // ✅ 깃허브 아이디
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                commentResponses
        );
    }
}