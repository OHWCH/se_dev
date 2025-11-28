package com.example.gitrajabi.board.controller;

import com.example.gitrajabi.board.domain.Comment;
import com.example.gitrajabi.board.dto.CommentCreationRequest;
import com.example.gitrajabi.board.dto.CommentResponse;
import com.example.gitrajabi.board.service.CommentManagementService;
import com.example.gitrajabi.config.SecurityUtil; // ⭐ 추가
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
            // ⭐ @AuthenticationPrincipal OAuth2User 대신 SecurityUtil 사용을 가정합니다.
            @RequestBody CommentCreationRequest request
    ) {
        // ⭐ SecurityUtil을 사용하여 userId를 가져옵니다.
        Long currentUserId = SecurityUtil.getCurrentUserId();

        try {
            Comment createdComment = commentManagementService.createComment(postId, currentUserId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(CommentResponse.from(createdComment));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Use Case #21: DELETE /api/posts/{postId}/comments/{commentId} - 댓글 삭제 (인증 필요)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            // ⭐ @AuthenticationPrincipal OAuth2User 제거
            @PathVariable Long commentId
    ) {
        // ⭐ SecurityUtil을 사용하여 userId를 가져옵니다.
        Long currentUserId = SecurityUtil.getCurrentUserId();

        try {
            commentManagementService.deleteComment(currentUserId, commentId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}