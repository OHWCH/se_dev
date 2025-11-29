package com.example.gitrajabi.board.dto;

import com.example.gitrajabi.board.domain.Comment;

import java.time.LocalDateTime;

// Comment 응답 시 사용
public record CommentResponse(
        Long commentId,
        Long postId,
        String content,
        Long userId, // 작성자 ID
        // String userName, // ❌ 제거: 주석 처리된 부분 포함
        LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getCommentId(),
                comment.getPostId(),
                comment.getContent(),
                comment.getUserId(),
                // "임시 사용자 이름", // ❌ 제거: 임시 사용자 이름 필드 제거
                comment.getCreatedAt()
        );
    }
}