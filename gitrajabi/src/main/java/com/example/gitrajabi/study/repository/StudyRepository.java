package com.example.gitrajabi.study.repository;


import com.example.gitrajabi.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudyRepository extends JpaRepository<Study, Long> {

    Page<Study> findAllByIsDeletedFalse(Pageable pageable);
}
