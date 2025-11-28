package com.example.gitrajabi.board.controller;

import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.dto.PostCreationRequest;
import com.example.gitrajabi.board.dto.PostResponse;
import com.example.gitrajabi.board.dto.PostUpdateRequest;
import com.example.gitrajabi.board.service.PostManagementService;
import com.example.gitrajabi.board.service.PostQueryService;
import com.example.gitrajabi.user_login.common.security.SecurityUtil; // ⭐️ JWT userId 추출 유틸리티 추가
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.core.annotation.AuthenticationPrincipal; // ❌ 삭제
// import org.springframework.security.oauth2.core.user.OAuth2User; // ❌ 삭제
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
        List<PostResponse> response = postQueryService.getPostList(type, page).stream()
                .map(PostResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    // Use Case #18: GET /api/posts/{postId} - 게시글 상세 조회 (조회수 증가 포함, 인증 필요 없음)
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostDetail(@PathVariable Long postId) throws Throwable {
        Post post = postQueryService.getPostDetail(postId);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    // Use Case #14: POST /api/posts - 게시글 작성 (인증 필요)
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
//            @AuthenticationPrincipal OAuth2User oauthUser, // 삭제
            @RequestBody PostCreationRequest request
    ) {
        // ⭐️ JWT에서 현재 로그인된 사용자의 ID를 가져옵니다.
        Long currentUserId = SecurityUtil.getCurrentUserId();

        Post createdPost = postManagementService.createPost(currentUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(createdPost));
    }

    // Use Case #15: PUT /api/posts/{postId} - 게시글 수정 (인증 필요)
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
//            @AuthenticationPrincipal OAuth2User oauthUser, // 삭제
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request
    ) {
        // ⭐️ JWT에서 현재 로그인된 사용자의 ID를 가져옵니다.
        Long currentUserId = SecurityUtil.getCurrentUserId();
//        Long currentUserId = Long.valueOf(oauthUser.getAttribute("id").toString()); // 삭제

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
//            @AuthenticationPrincipal OAuth2User oauthUser, // 삭제
            @PathVariable Long postId
    ) {
        // ⭐️ JWT에서 현재 로그인된 사용자의 ID를 가져옵니다.
        Long currentUserId = SecurityUtil.getCurrentUserId();
//        Long currentUserId = Long.valueOf(oauthUser.getAttribute("id").toString()); // 삭제

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