package com.example.gitrajabi.board.controller;

import com.example.gitrajabi.board.domain.Comment;
import com.example.gitrajabi.board.dto.CommentCreationRequest;
import com.example.gitrajabi.board.dto.CommentResponse;
import com.example.gitrajabi.board.service.CommentManagementService;
import com.example.gitrajabi.user.security.SecurityUtil;
import com.example.gitrajabi.user.domain.entity.UserEntity; // ⭐️ 추가: UserEntity import
import com.example.gitrajabi.user.domain.repository.UserRepository; // ⭐️ 추가: UserRepository import
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentManagementService commentManagementService;
    private final UserRepository userRepository; // ⭐️ 추가: UserRepository 필드 선언

    public CommentController(CommentManagementService commentManagementService, UserRepository userRepository) { // ⭐️ 수정: 생성자에 UserRepository 추가
        this.commentManagementService = commentManagementService;
        this.userRepository = userRepository; // ⭐️ 추가: 초기화
    }

    // Use Case #20: POST /api/posts/{postId}/comments - 댓글 작성 (인증 필요)
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @RequestBody CommentCreationRequest request
    ) {
        Long currentUserId = SecurityUtil.getCurrentUserId();

        try {
            Comment createdComment = commentManagementService.createComment(postId, currentUserId, request);

            // ⭐️ 오류 수정 (36 line): UserRepository를 사용하여 githubId를 조회
            String githubId = userRepository.findById(currentUserId)
                    .map(UserEntity::getGithubId)
                    .orElse("익명"); // 사용자를 찾지 못할 경우 기본값 처리

            return ResponseEntity.status(HttpStatus.CREATED).body(CommentResponse.from(createdComment, githubId)); // ⭐️ 수정: githubId 인자 추가
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Use Case #21: DELETE /api/posts/{postId}/comments/{commentId} - 댓글 삭제 (인증 필요)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
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