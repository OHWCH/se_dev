package com.example.gitrajabi.board.controller;

import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.dto.PostCreationRequest;
import com.example.gitrajabi.board.dto.PostResponse;
import com.example.gitrajabi.board.dto.PostUpdateRequest;
import com.example.gitrajabi.board.service.PostManagementService;
import com.example.gitrajabi.board.service.PostQueryService;
import com.example.gitrajabi.config.SecurityUtil; // ⭐ 추가
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostQueryService postQueryService;
    private final PostManagementService postManagementService;

    public PostController(PostQueryService postQueryService, PostManagementService postManagementService) {
        this.postQueryService = postQueryService;
        this.postManagementService = postManagementService;
    }

    // Use Case #17: GET /api/posts - 게시글 목록 조회 (인증 필요 없음)
    @GetMapping
    public ResponseEntity<List<PostResponse>> getPostList(
            @RequestParam(defaultValue = "latest") String type,
            @RequestParam(defaultValue = "0") int page
    ) {
        // Use Case #17: 게시글 목록 조회
        List<Post> posts = postQueryService.getPostList(type, page);
        // DTO로 변환하여 반환
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::from)
                .toList();

        return ResponseEntity.ok(postResponses);
    }

    // Use Case #14: POST /api/posts - 게시글 작성 (인증 필요)
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            // ⭐ @AuthenticationPrincipal OAuth2User 대신 SecurityUtil 사용을 가정합니다.
            @RequestBody PostCreationRequest request
    ) {
        // ⭐ SecurityUtil을 사용하여 userId를 가져옵니다.
        Long currentUserId = SecurityUtil.getCurrentUserId();

        try {
            Post createdPost = postManagementService.createPost(currentUserId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(createdPost));
        } catch (IllegalStateException e) {
            // SecurityUtil에서 인증 정보가 없을 때 던지는 예외 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Use Case #15: PUT /api/posts/{postId} - 게시글 수정 (인증 필요)
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            // ⭐ @AuthenticationPrincipal OAuth2User 제거
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request
    ) {
        // ⭐ SecurityUtil을 사용하여 userId를 가져옵니다.
        Long currentUserId = SecurityUtil.getCurrentUserId();

        try {
            Post updatedPost = postManagementService.updatePost(currentUserId, postId, request);
            return ResponseEntity.ok(PostResponse.from(updatedPost));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden: 권한 없음
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    // Use Case #16: DELETE /api/posts/{postId} - 게시글 삭제 (인증 필요)
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            // ⭐ @AuthenticationPrincipal OAuth2User 제거
            @PathVariable Long postId
    ) {
        // ⭐ SecurityUtil을 사용하여 userId를 가져옵니다.
        Long currentUserId = SecurityUtil.getCurrentUserId();

        try {
            postManagementService.deletePost(currentUserId, postId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden: 권한 없음
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}