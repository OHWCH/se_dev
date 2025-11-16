package com.example.gitrajabi.study.service;

import com.example.gitrajabi.study.dto.StudyCreateDto;
import com.example.gitrajabi.study.entity.Study;
import com.example.gitrajabi.study.entity.StudyMember;
import com.example.gitrajabi.study.entity.User;
import com.example.gitrajabi.study.erum.JoinStatus;
import com.example.gitrajabi.study.erum.StudyRole;
import com.example.gitrajabi.study.repository.StudyMemberRepository;
import com.example.gitrajabi.study.repository.StudyRepository;
import com.example.gitrajabi.study.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createStudy(StudyCreateDto request) {

        // 1) 리더 유저 조회
        User leader = userRepository.findById(request.getLeaderId())
                .orElseThrow(() -> new IllegalArgumentException("리더 유저가 존재하지 않습니다."));

        LocalDateTime now = LocalDateTime.now();

        // 2) Study 엔티티 생성
        Study study = Study.builder()
                .leader(leader)
                .name(request.getStudyName())
                .description(request.getStudyDescription())
                .category(request.getStudyCategory())
                .maxMemberCount(request.getMaxMembers())
                .createdAt(now)
                .updatedAt(now)
                .isDeleted(false)
                .build();

        // 3) 스터디 저장
        Study savedStudy = studyRepository.save(study);

        // 4) 스터디 리더 자동 StudyMember 등록
        StudyMember leaderMember = StudyMember.builder()
                .study(savedStudy)
                .user(leader)
                .studyRole(StudyRole.LEADER)
                .joinStatus(JoinStatus.APPROVED)
                .build();

        studyMemberRepository.save(leaderMember);

        return savedStudy.getStudyId();
    }
}

