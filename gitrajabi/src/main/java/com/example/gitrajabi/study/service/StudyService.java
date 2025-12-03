package com.example.gitrajabi.study.service;

import com.example.gitrajabi.study.dto.*;
import com.example.gitrajabi.study.entity.Study;
import com.example.gitrajabi.study.entity.StudyMember;
import com.example.gitrajabi.study.entity.StudySchedule;
import com.example.gitrajabi.study.erum.JoinStatus;
import com.example.gitrajabi.study.erum.StudyCategory;
import com.example.gitrajabi.study.erum.StudyRole;
import com.example.gitrajabi.study.repository.ScheduleParticipateRepository;
import com.example.gitrajabi.study.repository.StudyMemberRepository;
import com.example.gitrajabi.study.repository.StudyRepository;
import com.example.gitrajabi.study.repository.StudyScheduleRepository;
import com.example.gitrajabi.user.domain.entity.UserEntity;
import com.example.gitrajabi.user.domain.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final StudyScheduleRepository studyScheduleRepository;
    private final ScheduleParticipateRepository scheduleParticipateRepository;


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
    public Page<StudyListResponse> getStudyList(Long userId, Pageable pageable) {

        Page<Study> studyPage = studyRepository.findAllByIsDeletedFalse(pageable);

        return studyPage.map(study -> {

            int currentMembers = studyMemberRepository
                    .countByStudy_StudyIdAndJoinStatus(study.getStudyId(), JoinStatus.APPROVED);

            JoinStatus userJoinStatus = studyMemberRepository
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
        });
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

    @Transactional(readOnly = true)
    public StudyMainPageResponse getStudyMainPage(Long studyId) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));

        // 현재 인원수
        int currentMembers = studyMemberRepository
                .countByStudy_StudyIdAndJoinStatus(studyId, JoinStatus.APPROVED);

        // 멤버 목록 조회
        List<StudyMember> members = studyMemberRepository
                .findByStudy_StudyIdAndJoinStatus(studyId, JoinStatus.APPROVED);

        List<StudyMemberResponse> memberResponses = members.stream()
                .map(m -> StudyMemberResponse.builder()
                        .userId(m.getUser().getUserId())
                        .githubId(m.getUser().getGithubId())
                        .joinStatus(m.getJoinStatus())
                        .studyRole(m.getStudyRole().name())
                        .build())
                .toList();

        // 스케줄 목록 조회
        List<StudySchedule> schedules =
                studyScheduleRepository.findByStudy_StudyId(studyId);

        List<StudyMainScheduleResponse> scheduleResponses = schedules.stream()
                .map(s -> StudyMainScheduleResponse.builder()
                        .scheduleId(s.getScheduleId())
                        .comment(s.getComment())
                        .startedAt(s.getStartedAt().toString())
                        .endAt(s.getEndAt().toString())
                        .build())
                .toList();


        return StudyMainPageResponse.builder()
                .studyId(study.getStudyId())
                .studyName(study.getName())
                .studyDescription(study.getDescription())
                .studyCategory(study.getCategory().name())
                .currentMembers(currentMembers)
                .maxMembers(study.getMaxMemberCount())
                .leaderGithubId(study.getLeader().getGithubId())
                .members(memberResponses)
                .schedules(scheduleResponses)
                .build();
    }

    @Transactional
    public void deleteStudy(Long studyId, Long userId) {

        // 1) 스터디 존재 여부 확인
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));

        // 2) 스터디장 여부 검증 (선택)
        if (!study.getLeader().getUserId().equals(userId)) {
            throw new IllegalStateException("Only leader can delete study");
        }

        // 3) 일정들 가져오기
        List<StudySchedule> schedules = studyScheduleRepository.findAllByStudy_StudyId(studyId);

        // 4) 각 일정의 참여자 삭제
        for (StudySchedule schedule : schedules) {
            scheduleParticipateRepository.deleteBySchedule_ScheduleId(schedule.getScheduleId());
        }

        // 5) 일정 삭제
        studyScheduleRepository.deleteByStudy_StudyId(studyId);

        // 6) 스터디 멤버 삭제
        studyMemberRepository.deleteByStudy_StudyId(studyId);

        // 7) 스터디 삭제
        studyRepository.delete(study);
    }

    // 스터디 검색
    @Transactional(readOnly = true)
    public Page<StudyListResponse> searchStudies(
            String keyword,
            String category,
            Long userId,
            Pageable pageable
    ) {

        StudyCategory catEnum = null;
        if (category != null && !category.isBlank()) {
            catEnum = StudyCategory.valueOf(category.toUpperCase());
        }

        Page<Study> studyPage =
                studyRepository.searchStudies(keyword, catEnum, pageable);

        return studyPage.map(study -> {

            // 승인된 인원 수
            int currentMembers = studyMemberRepository
                    .countByStudy_StudyIdAndJoinStatus(study.getStudyId(), JoinStatus.APPROVED);

            // 현재 유저의 가입 여부
            JoinStatus userJoinStatus = studyMemberRepository
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
        });
    }

}
