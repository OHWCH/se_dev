package com.example.gitrajabi.board.controller;

import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.dto.PostCreationRequest;
import com.example.gitrajabi.board.dto.PostResponse;
import com.example.gitrajabi.board.dto.PostUpdateRequest;
import com.example.gitrajabi.board.service.PostManagementService;
import com.example.gitrajabi.board.service.PostQueryService;
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
            @RequestParam(required = false, defaultValue = "latest") String type,
            @RequestParam(required = false, defaultValue = "0") int page
    ) {
        List<Post> posts = postQueryService.getPostList(type, page);
        List<PostResponse> response = posts.stream()
                .map(PostResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    // Use Case #18: GET /api/posts/{postId} - 게시글 상세 조회 (인증 필요 없음)
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostDetail(@PathVariable Long postId) {
        try {
            Post post = postQueryService.getPostDetail(postId);
            return ResponseEntity.ok(PostResponse.from(post));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    // Use Case #14: POST /api/posts - 게시글 작성 (인증 필요)
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            // GitHub OAuth로 로그인한 사용자의 정보를 가져옵니다.
            @AuthenticationPrincipal OAuth2User oauthUser,
            @RequestBody PostCreationRequest request
    ) {
        // OAuth2User에서 GitHub ID(Long)를 추출합니다.
        // GitHub 'id' 속성은 Integer/Long 형태로 제공됩니다.
        Long currentUserId = Long.valueOf(oauthUser.getAttribute("id").toString());

        try {
            Post createdPost = postManagementService.createPost(currentUserId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(createdPost));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Use Case #15: PUT /api/posts/{postId} - 게시글 수정 (인증 필요)
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @AuthenticationPrincipal OAuth2User oauthUser,
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request
    ) {
        Long currentUserId = Long.valueOf(oauthUser.getAttribute("id").toString());

        try {
            Post updatedPost = postManagementService.updatePost(currentUserId, postId, request);
            return ResponseEntity.ok(PostResponse.from(updatedPost));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden: 권한 없음
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    // Use Case #16: DELETE /api/posts/{postId} - 게시글 삭제 (인증 필요)
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal OAuth2User oauthUser,
            @PathVariable Long postId
    ) {
        Long currentUserId = Long.valueOf(oauthUser.getAttribute("id").toString());

        try {
            postManagementService.deletePost(currentUserId, postId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden: 권한 없음
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}