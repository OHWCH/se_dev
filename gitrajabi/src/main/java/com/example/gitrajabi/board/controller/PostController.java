package com.example.gitrajabi.board.controller;

import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.dto.PostCreationRequest;
import com.example.gitrajabi.board.dto.PostDetailResponse;
import com.example.gitrajabi.board.dto.PostResponse; // ✅ PostResponse 임포트
import com.example.gitrajabi.board.dto.PostUpdateRequest;
import com.example.gitrajabi.board.service.PostManagementService;
import com.example.gitrajabi.board.service.PostQueryService;
import com.example.gitrajabi.user.security.SecurityUtil; // ✅ SecurityUtil import 추가
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.core.annotation.AuthenticationPrincipal; // ❌ 제거
// import org.springframework.security.oauth2.core.user.OAuth2User; // ❌ 제거
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
    // ✅ 변경: List<Post> -> List<PostResponse>
    public ResponseEntity<List<PostResponse>> getPostList(
            @RequestParam(defaultValue = "createdAt") String type,
            @RequestParam(defaultValue = "0") int page
    ) {
        List<PostResponse> postList = postQueryService.getPostList(type, page);
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
        Long currentUserId = SecurityUtil.getCurrentUserId(); // ✅ 변경: SecurityUtil 사용

        Post createdPost = postManagementService.createPost(currentUserId, request);
        // ✅ PostResponse.from(Post, commentCount) 형식에 맞게 0을 전달 (새 게시글이므로 댓글 0개)
        return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(createdPost, 0));
    }

    // Use Case #15: PUT /api/posts/{postId} - 게시글 수정 (인증 필요)
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            // @AuthenticationPrincipal OAuth2User oauthUser, // ❌ 제거
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request
    ) {
        Long currentUserId = SecurityUtil.getCurrentUserId(); // ✅ 변경: SecurityUtil 사용

        try {
            Post updatedPost = postManagementService.updatePost(currentUserId, postId, request);
            // ✅ PostResponse.from(Post, commentCount) 형식에 맞게 0을 전달 (수정 시 댓글 개수는 변하지 않으므로 임시로 0 처리)
            return ResponseEntity.ok(PostResponse.from(updatedPost, 0));
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
        Long currentUserId = SecurityUtil.getCurrentUserId(); // ✅ 변경: SecurityUtil 사용

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