package com.example.gitrajabi.board.dto;

import com.example.gitrajabi.board.domain.Comment;
import java.time.LocalDateTime;

// Comment 응답 시 사용
public record CommentResponse(
        Long commentId,
        Long postId,
        String content,
        Long userId, // 작성자 ID
        String authorGithubId, // ✅ 추가: 작성자 깃허브 아이디
        LocalDateTime createdAt
) {
    // ✅ 수정: 깃허브 아이디를 인자로 받도록 변경
    public static CommentResponse from(Comment comment, String githubId) {
        return new CommentResponse(
                comment.getCommentId(),
                comment.getPostId(),
                comment.getContent(),
                comment.getUserId(),
                githubId, // ✅ 깃허브 아이디 사용
                comment.getCreatedAt()
        );
    }

    // 기본 from 메서드는 삭제하고, 깃허브 아이디를 포함하는 메서드만 사용하도록 PostQueryService에서 처리합니다.
}