package com.example.gitrajabi.board.repository;

import com.example.gitrajabi.board.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    // ⭐️ Comment 엔티티의 Long postId 필드를 사용하여 댓글 목록을 조회
    // JPA의 쿼리 메서드 기능을 사용하여 post_id 칼럼으로 쿼리를 생성합니다.
    List<Comment> findByPostId(Long postId);

    // ✅ 추가: 여러 게시글 ID에 대한 댓글 수를 한 번에 조회하는 쿼리
    // 반환 타입은 [postId (Long), commentCount (Long)]의 Object[] 리스트입니다.
    @Query("SELECT c.postId, COUNT(c.commentId) FROM Comment c WHERE c.postId IN :postIds AND c.deletedAt IS NULL GROUP BY c.postId")
    List<Object[]> countCommentsByPostIds(@Param("postIds") List<Long> postIds);
}