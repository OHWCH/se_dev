package com.example.gitrajabi.board.service;

import com.example.gitrajabi.board.domain.Comment;
import com.example.gitrajabi.board.dto.CommentCreationRequest;
import com.example.gitrajabi.board.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CommentManagementService {

    private final CommentRepository commentRepository;

    public CommentManagementService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // Use Case #20: 댓글 작성
    public Comment createComment(Long postId, Long currentUserId, CommentCreationRequest request) {
        //  Comment 생성자에 Long postId와 Long currentUserId를 전달합니다.
        Comment comment = new Comment(postId, currentUserId, request.content());
        return commentRepository.save(comment);
    }

    // Use Case #21: 댓글 삭제 (본인 댓글, 소프트 삭제)
    public void deleteComment(Long currentUserId, Long commentId) throws Throwable {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다."));

        if (!comment.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("삭제 권한이 없습니다. 본인 댓글만 삭제 가능합니다.");
        }

        comment.softDelete(); // 소프트 삭제 플래그 설정
        // @Transactional에 의해 자동 저장 처리됨
    }
}