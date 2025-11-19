package com.example.gitrajabi.study.service;

import com.example.gitrajabi.study.dto.StudyScheduleCreateRequest;
import com.example.gitrajabi.study.entity.*;
import com.example.gitrajabi.study.erum.JoinStatus;
import com.example.gitrajabi.study.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyScheduleService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyScheduleRepository studyScheduleRepository;
    private final ScheduleParticipateRepository scheduleParticipantRepository;

    @Transactional
    public Long createSchedule(Long studyId, Long userId, StudyScheduleCreateRequest request) {

        // 1) 스터디 조회
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));

        // 2) 현재 유저가 스터디장인지 확인
        if (!study.getLeader().getId().equals(userId)) {
            throw new IllegalArgumentException("스터디장만 일정을 생성할 수 있습니다.");
        }

        // 3) 일정 생성
        StudySchedule schedule = StudySchedule.builder()
                .study(study)
                .comment(request.getComment())
                .startedAt(request.getStartedAt())
                .endAt(request.getEndAt())
                .createdAt(LocalDateTime.now())
                .deletedAt(null)
                .build();



        StudySchedule saved = studyScheduleRepository.save(schedule);

        // 4) 일정 생성자인 스터디장을 자동 참석 등록
        ScheduleParticipate leaderParticipate = ScheduleParticipate.builder()
                .schedule(saved)
                .user(study.getLeader())
                .build();


        scheduleParticipantRepository.save(leaderParticipate);

        return saved.getScheduleId();
    }
}
