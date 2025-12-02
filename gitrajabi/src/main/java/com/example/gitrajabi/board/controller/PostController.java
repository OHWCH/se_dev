package com.example.gitrajabi.board.controller;

import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.dto.PostCreationRequest;
import com.example.gitrajabi.board.dto.PostDetailResponse; // 🌟 PostDetailResponse 임포트
import com.example.gitrajabi.board.dto.PostPageResponse; // ✅ 추가: PostPageResponse 임포트
import com.example.gitrajabi.board.dto.PostResponse;
import com.example.gitrajabi.board.dto.PostUpdateRequest;
import com.example.gitrajabi.board.service.PostManagementService;
import com.example.gitrajabi.board.service.PostQueryService;
import com.example.gitrajabi.user.security.SecurityUtil; // ✅ 추가: SecurityUtil import
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.core.annotation.AuthenticationPrincipal; // ❌ 삭제: SecurityUtil 사용
// import org.springframework.security.oauth2.core.user.OAuth2User; // ❌ 삭제: SecurityUtil 사용
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

    // Use Case #17: GET /api/posts - 게시글 목록 조회 (페이지네이션)
    @GetMapping
    public ResponseEntity<PostPageResponse> getPostList( // ✅ 반환 타입 PostPageResponse
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size, // ✅ 수정: size 파라미터 추가 (기본값 10)
                                                         @RequestParam(defaultValue = "createdAt") String sort
    ) {
        // ✅ 수정: size 인자를 service에 전달
        PostPageResponse postsPage = postQueryService.getPostList(page, size, sort);
        return ResponseEntity.ok(postsPage);
    }

    // Use Case #18: GET /api/posts/{postId} - 게시글 상세 조회 (댓글 포함)
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long postId) {
        try {
            PostDetailResponse postDetail = postQueryService.getPostDetail(postId);
            return ResponseEntity.ok(postDetail);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Use Case #14: POST /api/posts - 게시글 작성 (인증 필요)
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            // @AuthenticationPrincipal OAuth2User oauthUser, // ❌ 삭제: SecurityUtil 사용
            @RequestBody PostCreationRequest request
    ) {
        // OAuth2User 대신, SecurityContext에서 userId를 가져옵니다.
        Long currentUserId = SecurityUtil.getCurrentUserId(); // ✅ 변경: SecurityUtil 사용

        Post createdPost = postManagementService.createPost(currentUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(createdPost)); // ✅ from(Post post) 호출
    }

    // Use Case #15: PUT /api/posts/{postId} - 게시글 수정 (인증 필요)
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            // @AuthenticationPrincipal OAuth2User oauthUser, // ❌ 삭제: SecurityUtil 사용
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request
    ) {
        Long currentUserId = SecurityUtil.getCurrentUserId(); // ✅ 변경: SecurityUtil 사용

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
            // @AuthenticationPrincipal OAuth2User oauthUser, // ❌ 삭제: SecurityUtil 사용
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