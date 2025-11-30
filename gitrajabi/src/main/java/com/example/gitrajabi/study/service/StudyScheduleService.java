package com.example.gitrajabi.study.service;



import com.example.gitrajabi.study.dto.ScheduleListResponse;
import com.example.gitrajabi.study.dto.StudyScheduleCreateRequest;
import com.example.gitrajabi.study.entity.ScheduleParticipate;
import com.example.gitrajabi.study.entity.Study;
import com.example.gitrajabi.study.entity.StudyMember;
import com.example.gitrajabi.study.entity.StudySchedule;
import com.example.gitrajabi.study.erum.JoinStatus;
import com.example.gitrajabi.study.repository.ScheduleParticipateRepository;
import com.example.gitrajabi.study.repository.StudyMemberRepository;
import com.example.gitrajabi.study.repository.StudyRepository;
import com.example.gitrajabi.study.repository.StudyScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyScheduleService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyScheduleRepository studyScheduleRepository;
    private final ScheduleParticipateRepository scheduleParticipantRepository;

    @Transactional
    public Long createSchedule(Long studyId, Long userId, StudyScheduleCreateRequest request) {

        // 스터디 조회
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));

        // 현재 유저가 스터디장인지 확인
        if (!study.getLeader().getUserId().equals(userId)) {
            throw new IllegalArgumentException("스터디장만 일정을 생성할 수 있습니다.");
        }


        // 일정 생성
        StudySchedule schedule = StudySchedule.builder()
                .study(study)
                .comment(request.getComment())
                .startedAt(request.getStartedAt())
                .endAt(request.getEndAt())
                .createdAt(LocalDateTime.now())
                .deletedAt(null)
                .build();



        StudySchedule saved = studyScheduleRepository.save(schedule);

        // 스터디장을 자동 참석 등록
        ScheduleParticipate leaderParticipate = ScheduleParticipate.builder()
                .schedule(saved)
                .user(study.getLeader())
                .build();


        scheduleParticipantRepository.save(leaderParticipate);

        return saved.getScheduleId();
    }
    @Transactional
    public void participate(Long studyId, Long scheduleId, Long userId) {

        // 스터디 가입 여부 + 승인 여부 확인
        StudyMember member = studyMemberRepository
                .findByStudy_StudyIdAndUser_Id(studyId, userId)
                .orElseThrow(() -> new IllegalArgumentException("스터디에 가입된 유저가 아닙니다."));

        if (member.getJoinStatus() != JoinStatus.APPROVED) {
            throw new IllegalArgumentException("승인된 스터디원만 참여할 수 있습니다.");
        }

        // 중복 참여 방지
        boolean alreadyJoin = scheduleParticipantRepository
                .existsBySchedule_ScheduleIdAndUser_Id(scheduleId, userId);

        if (alreadyJoin) {
            throw new IllegalArgumentException("이미 해당 일정에 참여한 유저입니다.");
        }

        // 일정 조회
        StudySchedule schedule = studyScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));

        // 참여 등록
        ScheduleParticipate participate = ScheduleParticipate.builder()
                .schedule(schedule)
                .user(member.getUser())
                .build();

        scheduleParticipantRepository.save(participate);
    }


    @Transactional
    public List<ScheduleListResponse> getScheduleList(Long studyId, Long userId) {

        // 해당 스터디의 전체 멤버 수
        int totalMembers = studyMemberRepository.countByStudy_StudyIdAndJoinStatus(studyId, JoinStatus.APPROVED);

        // 스터디 스케줄 전체 조회
        List<StudySchedule> schedules = studyScheduleRepository.findByStudy_StudyId(studyId);

        // 각 스케줄별 참여 여부 + 참여자 수 조회
        return schedules.stream().map(schedule -> {

            // 참여 인원 수
            int participateCount =
                    scheduleParticipantRepository.countBySchedule_ScheduleId(schedule.getScheduleId());

            // 이 유저가 참여했는지 여부
            boolean participated =
                    scheduleParticipantRepository.existsBySchedule_ScheduleIdAndUser_Id(
                            schedule.getScheduleId(), userId);

            return ScheduleListResponse.builder()
                    .scheduleId(schedule.getScheduleId())
                    .comment(schedule.getComment())
                    .startedAt(schedule.getStartedAt().toString())
                    .endAt(schedule.getEndAt().toString())
                    .participateCount(participateCount)
                    .totalMemberCount(totalMembers)
                    .isParticipated(participated)
                    .build();

        }).toList();
    }

}
