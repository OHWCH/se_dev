package com.example.gitrajabi.study.service;

import com.example.gitrajabi.study.dto.*;
import com.example.gitrajabi.study.entity.Study;
import com.example.gitrajabi.study.entity.StudyMember;
import com.example.gitrajabi.study.entity.User;
import com.example.gitrajabi.study.erum.JoinStatus;
import com.example.gitrajabi.study.erum.StudyRole;
import com.example.gitrajabi.study.repository.StudyMemberRepository;
import com.example.gitrajabi.study.repository.StudyRepository;
import com.example.gitrajabi.study.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final UserRepository userRepository;
    private final StudyMemberService studyMemberService;


    // 스터디 생성
    @Transactional
    public Long createStudy(StudyCreateDto request, Long leaderId) {

        // 리더 유저 조회
        User leader = userRepository.findById(leaderId)
                .orElseThrow(() -> new IllegalArgumentException("리더 유저가 존재하지 않습니다."));

        LocalDateTime now = LocalDateTime.now();

        // Study 엔티티 생성
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

        // 저장
        Study savedStudy = studyRepository.save(study);

        // 스터디 리더를 자동으로 StudyMember 등록
        StudyMember leaderMember = StudyMember.builder()
                .study(savedStudy)
                .user(leader)
                .studyRole(StudyRole.LEADER)
                .joinStatus(JoinStatus.APPROVED)
                .build();

        studyMemberRepository.save(leaderMember);

        return savedStudy.getStudyId();
    }




    // 스터디 목록 조회
    public List<StudyListResponse> getStudyList(Long userId) {

        List<Study> studies = studyRepository.findAllByIsDeletedFalse();

        return studies.stream()
                .map(study -> {

                    // 현재 승인된 멤버 수
                    int currentMembers = studyMemberRepository.countByStudy_StudyIdAndJoinStatus(
                            study.getStudyId(),
                            JoinStatus.APPROVED
                    );

                    // 해당 유저의 스터디 참여 상태 (없으면 null)
                    JoinStatus userJoinStatus = studyMemberRepository
                            .findByStudy_StudyIdAndUser_Id(study.getStudyId(), userId)
                            .map(StudyMember::getJoinStatus)
                            .orElse(null);


                    return StudyListResponse.builder()
                            .studyId(study.getStudyId())
                            .name(study.getName())
                            .description(study.getDescription())
                            .currentMembers(currentMembers)
                            .maxMembers(study.getMaxMemberCount())
                            .userJoinStatus(userJoinStatus)
                            .build();
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public StudyManageResponse getManagePageInfo(Long studyId, Long userId) {

        // 1. 스터디 정보 조회
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));

        // 2. 스터디 정보 DTO 변환
        StudyInfoResponse info = StudyInfoResponse.builder()
                .studyId(study.getStudyId())
                .studyName(study.getName())
                .studyDescription(study.getDescription())
                .studyCategory(study.getCategory())
                .maxMemberCount(study.getMaxMemberCount())
                .build();

        // 3. 신청자 리스트 조회
        List<StudyApplicantResponse> applicants = studyMemberService.getApplicants(studyId);

        // 4. 통합 Response
        return StudyManageResponse.builder()
                .studyInfo(info)
                .applicants(applicants)
                .build();
    }


}

