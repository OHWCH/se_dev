package com.example.gitrajabi.study.service;



import com.example.gitrajabi.study.dto.*;
import com.example.gitrajabi.study.entity.Study;
import com.example.gitrajabi.study.entity.StudyMember;
import com.example.gitrajabi.study.erum.JoinStatus;
import com.example.gitrajabi.study.erum.StudyRole;
import com.example.gitrajabi.study.repository.StudyMemberRepository;
import com.example.gitrajabi.study.repository.StudyRepository;
import com.example.gitrajabi.user_login.domain.user.repository.UserRepository;
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

    // 내가 가입중인 스터디 목록 조회
    public List<StudyListResponse> getMyStudyList(Long userId) {

        // 해당 유저가 APPROVED 상태로 가입 중인 StudyMember 목록 조회
        List<StudyMember> myMembers = studyMemberRepository
                .findByUser_IdAndJoinStatus(userId, JoinStatus.APPROVED);

        // StudyMember → Study 엔티티 매핑하여 DTO 생성
        return myMembers.stream()
                .map(member -> {

                    Study study = member.getStudy();

                    // 현재 승인된 멤버 수
                    int currentMembers = studyMemberRepository.countByStudy_StudyIdAndJoinStatus(
                            study.getStudyId(),
                            JoinStatus.APPROVED
                    );

                    // 나의 JoinStatus = 무조건 APPROVED (조회 조건에서 제한)
                    JoinStatus userJoinStatus = JoinStatus.APPROVED;

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


    // 스터디 관리페이지
    @Transactional(readOnly = true)
    public StudyManageResponse getManagePageInfo(Long studyId, Long userId) {

        //  스터디 정보 조회
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));

        //  스터디 정보 DTO 변환
        StudyInfoResponse info = StudyInfoResponse.builder()
                .studyId(study.getStudyId())
                .studyName(study.getName())
                .studyDescription(study.getDescription())
                .studyCategory(study.getCategory())
                .maxMemberCount(study.getMaxMemberCount())
                .build();

        //  신청자 리스트 조회
        List<StudyApplicantResponse> applicants = studyMemberService.getApplicants(studyId);

        //  통합 Response
        return StudyManageResponse.builder()
                .studyInfo(info)
                .applicants(applicants)
                .build();
    }

    // 스터디 업데이트
    @Transactional
    public void updateStudy(Long studyId, Long userId, StudyUpdateDto request) {

        //  스터디 조회
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));

        //  현재 로그인 유저가 스터디장인지 검증
        if (!study.getLeader().getId().equals(userId)) {
            throw new IllegalArgumentException("스터디장만 스터디 정보를 수정할 수 있습니다.");
        }

        //  필드 수정
        study.setName(request.getStudyName());
        study.setDescription(request.getStudyDescription());
        study.setCategory(request.getStudyCategory());
        study.setMaxMemberCount(request.getMaxMembers());
        study.setUpdatedAt(LocalDateTime.now());

    }



}

