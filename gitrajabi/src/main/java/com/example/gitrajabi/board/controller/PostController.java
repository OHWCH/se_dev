package com.example.gitrajabi.board.controller;

import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.dto.PostCreationRequest;
import com.example.gitrajabi.board.dto.PostDetailResponse; // 🌟 PostDetailResponse 임포트
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
import com.example.gitrajabi.board.dto.PostPageResponse; // ✅ 추가: PostPageResponse 임포트

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

    // Use Case #17: GET /api/posts - 게시글 목록 조회 (페이지 0부터, size 10, 최신순)
    @GetMapping
    public ResponseEntity<PostPageResponse> getPostList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort
    ) {
        // 목록 조회는 인증 없이 누구나 접근 가능
        PostPageResponse response = PostPageResponse.from(postQueryService.getPostList(page, size, sort));
        return ResponseEntity.ok(response);
    }

    // Use Case #18: GET /api/posts/{postId} - 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long postId) {
        // 상세 조회는 인증 없이 누구나 접근 가능
        try {
            PostDetailResponse response = postQueryService.getPostDetail(postId);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Use Case #14: POST /api/posts - 게시글 작성 (인증 필요)
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            // JWT를 통해 SecurityContext에 저장된 userId를 가져옴
            @AuthenticationPrincipal OAuth2User oauthUser, // ❌ SecurityUtil 사용으로 변경하는게 더 좋음
            @RequestBody PostCreationRequest request
    ) {
        // OAuth2User 대신, SecurityContext에서 userId를 가져와야 합니다.
        // 현재 코드에서는 OAuth2User를 사용하고 있으므로, 그에 맞게 처리합니다.
        Long currentUserId = Long.valueOf(oauthUser.getAttribute("id").toString());

        try {
            Post createdPost = postManagementService.createPost(currentUserId, request);
            // 생성 응답은 content를 포함하는 PostResponse.from(Post post) 호출
            return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(createdPost));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    // Use Case #15: PUT /api/posts/{postId} - 게시글 수정 (인증 필요, 본인 글)
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @AuthenticationPrincipal OAuth2User oauthUser, // ❌ SecurityUtil 사용으로 변경하는게 더 좋음
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request
    ) {
        // OAuth2User 대신, SecurityContext에서 userId를 가져와야 합니다.
        Long currentUserId = Long.valueOf(oauthUser.getAttribute("id").toString());

        try {
            Post updatedPost = postManagementService.updatePost(currentUserId, postId, request);
            return ResponseEntity.ok(PostResponse.from(updatedPost)); // ✅ from(Post post) 호출
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
            @AuthenticationPrincipal OAuth2User oauthUser, // ❌ SecurityUtil 사용으로 변경하는게 더 좋음
            @PathVariable Long postId
    ) {
        // OAuth2User 대신, SecurityContext에서 userId를 가져와야 합니다.
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