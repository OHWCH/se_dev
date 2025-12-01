package com.example.gitrajabi.board.controller;

import com.example.gitrajabi.board.domain.Comment;
import com.example.gitrajabi.board.dto.CommentCreationRequest;
import com.example.gitrajabi.board.dto.CommentResponse;
import com.example.gitrajabi.board.service.CommentManagementService;
import com.example.gitrajabi.user.security.SecurityUtil; // ✅ SecurityUtil import 추가
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal; // ❌ 제거
// import org.springframework.security.oauth2.core.user.OAuth2User; // ❌ 제거
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentManagementService commentManagementService;

    public CommentController(CommentManagementService commentManagementService) {
        this.commentManagementService = commentManagementService;
    }

    // Use Case #20: POST /api/posts/{postId}/comments - 댓글 작성 (인증 필요)
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            // @AuthenticationPrincipal OAuth2User oauthUser, // ❌ 제거
            @RequestBody CommentCreationRequest request
    ) {
        // OAuth2User 대신, SecurityContext에서 userId를 가져옵니다.
        Long currentUserId = SecurityUtil.getCurrentUserId(); // ✅ 변경: SecurityUtil 사용

        try {
            Comment createdComment = commentManagementService.createComment(postId, currentUserId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(CommentResponse.from(createdComment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Use Case #21: DELETE /api/posts/{postId}/comments/{commentId} - 댓글 삭제 (인증 필요)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            // @AuthenticationPrincipal OAuth2User oauthUser, // ❌ 제거
            @PathVariable Long commentId
    ) {
        // OAuth2User 대신, SecurityContext에서 userId를 가져옵니다.
        Long currentUserId = SecurityUtil.getCurrentUserId(); // ✅ 변경: SecurityUtil 사용

        try {
            commentManagementService.deleteComment(currentUserId, commentId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}