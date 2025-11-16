package com.example.gitrajabi.study.repository;

import com.example.gitrajabi.study.entity.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
}
