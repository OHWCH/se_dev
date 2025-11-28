package com.example.gitrajabi.board.dto;

import com.example.gitrajabi.board.domain.Comment;

import java.time.LocalDateTime;

// Comment 응답 시 사용
public record CommentResponse(
        Long commentId,
        Long postId,
        String content,
        Long userId, // 작성자 ID
        //String nickname, // TODO: User 엔티티 연동 시 추가
        LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getCommentId(),
                comment.getPostId(),
                comment.getContent(),
                comment.getUserId(),
                 // TODO: User 엔티티 연동 시 수정
                comment.getCreatedAt()
        );
    }
}