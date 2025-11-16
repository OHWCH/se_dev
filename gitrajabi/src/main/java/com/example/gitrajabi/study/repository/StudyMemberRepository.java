package com.example.gitrajabi.study.repository;
import com.example.gitrajabi.study.entity.StudyMember;
import com.example.gitrajabi.study.erum.JoinStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    int countByStudy_StudyIdAndJoinStatus(Long studyId, JoinStatus joinStatus);
    Optional<StudyMember> findByStudy_StudyIdAndUser_Id(Long studyId, Long userId);
    boolean existsByStudy_StudyIdAndUser_Id(Long studyId, Long userId);



}
