package com.example.gitrajabi.board.controller;

import com.example.gitrajabi.board.domain.Comment;
import com.example.gitrajabi.board.dto.CommentCreationRequest;
import com.example.gitrajabi.board.dto.CommentResponse;
import com.example.gitrajabi.board.service.CommentManagementService;
import com.example.gitrajabi.user.domain.entity.UserEntity;
import com.example.gitrajabi.user.domain.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentManagementService commentManagementService;
    private final UserRepository userRepository;

    public CommentController(CommentManagementService commentManagementService, 
                             UserRepository userRepository) {
        this.commentManagementService = commentManagementService;
        this.userRepository = userRepository;
    }

    // ✅ [핵심] PostController와 동일한 자동 가입 로직 추가
    private UserEntity findOrCreateUser(String githubId) {
        return userRepository.findByGithubId(githubId)
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setGithubId(githubId);
                    // 필요한 초기값 설정 (UserEntity @PrePersist가 처리하므로 생략 가능)
                    return userRepository.save(newUser);
                });
    }

    // 1. 댓글 작성
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @RequestParam String githubId, // 프론트에서 보낸 githubId
            @RequestBody CommentCreationRequest request
    ) {
        try {
            // 1. 사용자 찾기 (없으면 자동 생성)
            UserEntity user = findOrCreateUser(githubId); 
            
            // 2. 댓글 생성 서비스 호출
            Comment createdComment = commentManagementService.createComment(postId, user.getUserId(), request);
            
            // 3. 응답 반환
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(CommentResponse.from(createdComment, githubId));
                    
        } catch (Throwable e) {
            e.printStackTrace(); // 서버 로그에 에러 출력
            // 400으로 뭉뚱그리지 않고 500 에러를 띄워서 원인을 명확히 함
            throw new RuntimeException("댓글 작성 중 오류 발생: " + e.getMessage()); 
        }
    }

    // 2. 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestParam String githubId
    ) {
        try {
            // 삭제 시에도 사용자가 존재해야 권한 체크 가능 (자동 생성 호출)
            UserEntity user = findOrCreateUser(githubId); 
            
            commentManagementService.deleteComment(user.getUserId(), commentId);
            return ResponseEntity.noContent().build();
            
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}