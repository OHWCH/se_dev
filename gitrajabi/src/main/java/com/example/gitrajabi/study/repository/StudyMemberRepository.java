package com.example.gitrajabi.study.repository;

import com.example.gitrajabi.study.entity.StudyMember;
import com.example.gitrajabi.study.erum.JoinStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {

    int countByStudy_StudyIdAndJoinStatus(Long studyId, JoinStatus joinStatus);

    Optional<StudyMember> findByStudy_StudyIdAndUser_UserId(Long studyId, Long userId);

    boolean existsByStudy_StudyIdAndUser_UserId(Long studyId, Long userId);

    List<StudyMember> findByStudy_StudyIdAndJoinStatus(Long studyId, JoinStatus joinStatus);

    List<StudyMember> findByStudy_StudyIdAndJoinStatusNot(Long studyId, JoinStatus joinStatus);

    int countByStudy_StudyIdAndJoinStatusIn(Long studyId, List<JoinStatus> statuses);

    List<StudyMember> findByUser_UserIdAndJoinStatus(Long userId, JoinStatus joinStatus);
}

