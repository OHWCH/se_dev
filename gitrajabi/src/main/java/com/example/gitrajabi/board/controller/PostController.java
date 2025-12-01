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
    public ResponseEntity<List<PostResponse>> getPostList(
            @RequestParam(defaultValue = "createdAt") String type, // 정렬 기준 (createdAt 또는 popular)
            @RequestParam(defaultValue = "0") int page
    ) {
        // ❌ 오류 수정: Service의 반환 타입 변경에 맞춰 변수 타입도 List<PostResponse>로 변경
        List<PostResponse> postList = postQueryService.getPostList(type, page);
        return ResponseEntity.ok(postList);
    }

    // Use Case #18: GET /api/posts/{postId} - 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetail(
            @PathVariable Long postId
    ) {
        try {
            // ✅ Service에서 조회수 증가 로직을 포함하여 상세 정보 반환
            PostDetailResponse postDetail = postQueryService.getPostDetailWithComments(postId);
            return ResponseEntity.ok(postDetail);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Use Case #14: POST /api/posts - 게시글 작성 (인증 필요)
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal OAuth2User oauthUser,
            @RequestBody PostCreationRequest request
    ) {
        // 사용자 ID를 가져오는 방식이 임시적으로 OAuth2User의 'id' 속성을 사용한다고 가정
        Long currentUserId = Long.valueOf(oauthUser.getAttribute("id").toString());

        Post createdPost = postManagementService.createPost(currentUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(createdPost));
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