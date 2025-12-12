package com.example.gitrajabi.board.controller;

import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.dto.*;
import com.example.gitrajabi.board.service.PostManagementService;
import com.example.gitrajabi.board.service.PostQueryService;
import com.example.gitrajabi.user.domain.entity.UserEntity;
import com.example.gitrajabi.user.domain.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostQueryService postQueryService;
    private final PostManagementService postManagementService;
    private final UserRepository userRepository;

    public PostController(PostQueryService postQueryService, 
                          PostManagementService postManagementService,
                          UserRepository userRepository) {
        this.postQueryService = postQueryService;
        this.postManagementService = postManagementService;
        this.userRepository = userRepository;
    }

    // ✅ [핵심] 사용자가 DB에 없으면 자동으로 생성하는 헬퍼 메서드
    private UserEntity findOrCreateUser(String githubId) {
        return userRepository.findByGithubId(githubId)
                .orElseGet(() -> {
                    // 사용자가 없으면 새로 생성해서 저장 (자동 회원가입)
                    UserEntity newUser = new UserEntity();
                    newUser.setGithubId(githubId); 
                    // 필요하다면 여기에 newUser.setRole("USER"); 등 추가 설정
                    return userRepository.save(newUser);
                });
    }

    // 1. 게시글 목록 조회
    @GetMapping
    public ResponseEntity<PostPageResponse> getPostList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort
    ) {
        PostPageResponse response = PostPageResponse.from(postQueryService.getPostList(page, size, sort));
        return ResponseEntity.ok(response);
    }

    // 2. 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long postId) {
        try {
            PostDetailResponse response = postQueryService.getPostDetail(postId);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. 게시글 작성
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestParam String githubId, 
            @RequestBody PostCreationRequest request
    ) {
        try {
            UserEntity user = findOrCreateUser(githubId); // ✅ 자동 가입 사용
            Post createdPost = postManagementService.createPost(user.getUserId(), request);
            return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(createdPost));
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // 4. 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long postId,
            @RequestParam String githubId,
            @RequestBody PostUpdateRequest request
    ) {
        try {
            UserEntity user = findOrCreateUser(githubId); // ✅ 자동 가입 사용
            Post updatedPost = postManagementService.updatePost(user.getUserId(), postId, request);
            return ResponseEntity.ok(PostResponse.from(updatedPost));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    // 5. 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @RequestParam String githubId
    ) {
        try {
            UserEntity user = findOrCreateUser(githubId); // ✅ 자동 가입 사용
            postManagementService.deletePost(user.getUserId(), postId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}