package com.example.gitrajabi.study.repository;


import com.example.gitrajabi.study.entity.Study;
import com.example.gitrajabi.study.erum.StudyCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyRepository extends JpaRepository<Study, Long> {

    Page<Study> findAllByIsDeletedFalse(Pageable pageable);

    @Query("""
        SELECT s FROM Study s
        WHERE s.isDeleted = false
        AND (:keyword IS NULL OR s.name LIKE %:keyword% OR s.description LIKE %:keyword%)
        AND (:category IS NULL OR s.category = :category)
        """)
    Page<Study> searchStudies(
            @Param("keyword") String keyword,
            @Param("category") StudyCategory category,
            Pageable pageable
    );
}
