package com.example.gitrajabi.IssueManagement.repository;

import com.example.gitrajabi.IssueManagement.domain.Todo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // userId로 조회하되, 최신순(Desc)으로 가져옵니다.
    // Slice를 반환하여 다음 페이지가 있는지 여부를 알 수 있습니다.
    Slice<Todo> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}