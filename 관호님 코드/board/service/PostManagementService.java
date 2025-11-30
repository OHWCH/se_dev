package com.example.gitrajabi.board.service;

import com.example.gitrajabi.board.domain.Post;
import com.example.gitrajabi.board.dto.PostCreationRequest;
import com.example.gitrajabi.board.dto.PostUpdateRequest;
import com.example.gitrajabi.board.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Transactional
public class PostManagementService {

    private final PostRepository postRepository;

    public PostManagementService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Use Case #14: 게시글 작성
    public Post createPost(Long currentUserId, PostCreationRequest request) {
        //  Post 생성자에 Long currentUserId를 전달
        Post post = new Post(currentUserId, request.title(), request.content());
        return postRepository.save(post);
    }

    // Use Case #15: 게시글 수정 (본인 글)
    public Post updatePost(Long currentUserId, Long postId, PostUpdateRequest request) throws Throwable {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        if (!post.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("수정 권한이 없습니다. 본인 글만 수정 가능합니다.");
        }

        post.update(request.title(), request.content());
        return post;
    }

    // Use Case #16: 게시글 삭제 (본인 글, 소프트 삭제)
    public void deletePost(Long currentUserId, Long postId) throws Throwable {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        if (!post.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("삭제 권한이 없습니다. 본인 글만 삭제 가능합니다.");
        }

        post.softDelete(); // 소프트 삭제 플래그 설정
        // @Transactional에 의해 자동 저장 처리됨
    }
}