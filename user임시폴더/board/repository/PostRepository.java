package com.example.gitrajabi.board.repository;

import com.example.gitrajabi.board.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {
    // findAll(Pageable)을 사용하여 정렬, 페이징, soft delete(@Where)를 모두 처리
    Page<Post> findAll(Pageable pageable);
}

