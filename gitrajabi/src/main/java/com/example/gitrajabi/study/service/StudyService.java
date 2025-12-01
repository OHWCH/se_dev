package com.example.gitrajabi.study.service;

import com.example.gitrajabi.study.dto.*;
import com.example.gitrajabi.study.entity.Study;
import com.example.gitrajabi.study.entity.StudyMember;
import com.example.gitrajabi.study.erum.JoinStatus;
import com.example.gitrajabi.study.erum.StudyRole;
import com.example.gitrajabi.study.repository.StudyMemberRepository;
import com.example.gitrajabi.study.repository.StudyRepository;
import com.example.gitrajabi.user.domain.entity.UserEntity;
import com.example.gitrajabi.user.domain.repository.UserRepository;

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

        UserEntity leader = userRepository.findById(leaderId)
                .orElseThrow(() -> new IllegalArgumentException("리더 유저가 존재하지 않습니다."));

        LocalDateTime now = LocalDateTime.now();

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

        Study savedStudy = studyRepository.save(study);

        // 자동 StudyMember 등록(리더)
        StudyMember leaderMember = StudyMember.builder()
                .study(savedStudy)
                .user(leader)
                .studyRole(StudyRole.LEADER)
                .joinStatus(JoinStatus.APPROVED)
                .build();

        studyMemberRepository.save(leaderMember);

        return savedStudy.getStudyId();
    }


    /** 스터디 리스트 */
    public List<StudyListResponse> getStudyList(Long userId) {

        List<Study> studies = studyRepository.findAllByIsDeletedFalse();

        return studies.stream()
                .map(study -> {

                    int currentMembers =
                            studyMemberRepository.countByStudy_StudyIdAndJoinStatus(
                                    study.getStudyId(), JoinStatus.APPROVED
                            );

                    JoinStatus userJoinStatus =
                            studyMemberRepository
                                    .findByStudy_StudyIdAndUser_UserId(study.getStudyId(), userId)
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

                }).toList();
    }


    /** 내가 가입한 스터디 목록 */
    public List<StudyListResponse> getMyStudyList(Long userId) {

        List<StudyMember> myMembers =
                studyMemberRepository.findByUser_UserIdAndJoinStatus(userId, JoinStatus.APPROVED);

        return myMembers.stream()
                .map(member -> {

                    Study study = member.getStudy();

                    int currentMembers =
                            studyMemberRepository.countByStudy_StudyIdAndJoinStatus(
                                    study.getStudyId(), JoinStatus.APPROVED
                            );

                    return StudyListResponse.builder()
                            .studyId(study.getStudyId())
                            .name(study.getName())
                            .description(study.getDescription())
                            .currentMembers(currentMembers)
                            .maxMembers(study.getMaxMemberCount())
                            .userJoinStatus(JoinStatus.APPROVED)
                            .build();
                }).toList();
    }


    /** 관리 페이지 조회 */
    @Transactional(readOnly = true)
    public StudyManageResponse getManagePageInfo(Long studyId, Long userId) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));

        StudyInfoResponse info = StudyInfoResponse.builder()
                .studyId(study.getStudyId())
                .studyName(study.getName())
                .studyDescription(study.getDescription())
                .studyCategory(study.getCategory())
                .maxMemberCount(study.getMaxMemberCount())
                .build();

        List<StudyApplicantResponse> applicants =
                studyMemberService.getApplicants(studyId);

        List<StudyMemberResponse> members =
                studyMemberService.getStudyMembers(studyId);  // 🔥 추가된 부분

        return StudyManageResponse.builder()
                .studyInfo(info)
                .applicants(applicants)
                .members(members)    // 🔥 추가된 부분
                .build();
    }



    /** 스터디 업데이트 */
    @Transactional
    public void updateStudy(Long studyId, Long userId, StudyUpdateDto request) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));

        if (!study.getLeader().getUserId().equals(userId)) {
            throw new IllegalArgumentException("스터디장만 수정할 수 있습니다.");
        }

        study.setName(request.getStudyName());
        study.setDescription(request.getStudyDescription());
        study.setCategory(request.getStudyCategory());
        study.setMaxMemberCount(request.getMaxMembers());
        study.setUpdatedAt(LocalDateTime.now());
    }
}
