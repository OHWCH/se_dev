package com.example.gitrajabi.board.dto;

import com.example.gitrajabi.board.domain.Comment;

import java.time.LocalDateTime;

// Comment 응답 시 사용
public record CommentResponse(
        Long commentId,
        Long postId,
        String content,
        Long userId, // 작성자 ID
        // String userName, // TODO: User 엔티티 연동 시 추가
        LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getCommentId(),
                comment.getPostId(),
                comment.getContent(),
                comment.getUserId(),
                // "임시 사용자 이름", // TODO: User 엔티티 연동 시 수정
                comment.getCreatedAt()
        );
    }
}