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
        // @Schema(description = "작성자 닉네임", example = "닉네임_1234") // ❌ 제거됨
        // String nickname, // 작성자 닉네임 // ❌ 제거됨
        @Schema(description = "조회수", example = "10")
        int viewCount,
        @Schema(description = "생성 일시", example = "2024-01-01T10:00:00")
        LocalDateTime createdAt,
        @Schema(description = "수정 일시", example = "2024-01-01T10:30:00")
        LocalDateTime updatedAt,

        @Schema(description = "댓글 목록")
        List<CommentResponse> comments // 🌟 상세 조회용: 댓글 리스트 추가
) {
    public static PostDetailResponse from(Post post, List<CommentResponse> commentResponses) {
        // String nickname = (post.getAuthor() != null) ? post.getAuthor().getNickname() : "탈퇴한 사용자"; // ❌ 닉네임 로직 제거

        return new PostDetailResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getUserId(),
                // nickname, // ❌ 닉네임 인자 제거
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                commentResponses
        );
    }
}