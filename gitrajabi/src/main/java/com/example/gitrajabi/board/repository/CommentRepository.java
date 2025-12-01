package com.example.gitrajabi.board.repository;

import com.example.gitrajabi.board.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    // ⭐️ Comment 엔티티의 Long postId 필드를 사용하여 댓글 목록을 조회
    // JPA의 쿼리 메서드 기능을 사용하여 post_id 칼럼으로 쿼리를 생성합니다.
    List<Comment> findByPostId(Long postId);

    // ✅ 추가: 특정 게시글 ID에 해당하는 댓글의 개수를 조회
    long countByPostId(Long postId);
}