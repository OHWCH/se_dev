package com.example.gitrajabi.board.repository;

import com.example.gitrajabi.board.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

}
