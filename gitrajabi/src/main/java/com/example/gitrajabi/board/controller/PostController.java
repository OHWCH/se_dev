package com.example.gitrajabi.board.controller;

import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.dto.PostCreationRequest;
import com.example.gitrajabi.board.dto.PostDetailResponse;
import com.example.gitrajabi.board.dto.PostResponse;
import com.example.gitrajabi.board.dto.PostUpdateRequest;
import com.example.gitrajabi.board.service.PostManagementService;
import com.example.gitrajabi.board.service.PostQueryService;
import com.example.gitrajabi.user.security.SecurityUtil; // ✅ SecurityUtil 임포트 추가
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

    // Use Case #17: GET /api/posts - 게시글 목록 조회
    @GetMapping
    public ResponseEntity<List<Post>> getPostList(
            @RequestParam(defaultValue = "createdAt") String type,
            @RequestParam(defaultValue = "0") int page
    ) {
        List<Post> postList = postQueryService.getPostList(type, page);
        return ResponseEntity.ok(postList);
    }

    // Use Case #18: GET /api/posts/{postId} - 게시글 상세 조회 (댓글 포함)
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long postId) {
        try {
            PostDetailResponse response = postQueryService.getPostDetailWithComments(postId);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Use Case #14: POST /api/posts - 게시글 작성 (인증 필요)
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            // @AuthenticationPrincipal OAuth2User oauthUser, // ❌ 제거
            @RequestBody PostCreationRequest request
    ) {
        // ✅ SecurityUtil을 사용하여 현재 사용자 ID 가져오기
        Long currentUserId = SecurityUtil.getCurrentUserId();

        Post createdPost = postManagementService.createPost(currentUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(createdPost));
    }

    // Use Case #15: PUT /api/posts/{postId} - 게시글 수정 (인증 필요)
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            // @AuthenticationPrincipal OAuth2User oauthUser, // ❌ 제거
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request
    ) {
        // ✅ SecurityUtil을 사용하여 현재 사용자 ID 가져오기
        Long currentUserId = SecurityUtil.getCurrentUserId();

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
            // @AuthenticationPrincipal OAuth2User oauthUser, // ❌ 제거
            @PathVariable Long postId
    ) {
        // ✅ SecurityUtil을 사용하여 현재 사용자 ID 가져오기
        Long currentUserId = SecurityUtil.getCurrentUserId();

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