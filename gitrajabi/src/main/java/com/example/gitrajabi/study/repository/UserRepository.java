package com.example.gitrajabi.study.repository;

import com.example.gitrajabi.study.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}