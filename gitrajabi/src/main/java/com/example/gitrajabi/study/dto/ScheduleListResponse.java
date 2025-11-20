package com.example.gitrajabi.study.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleListResponse {

    private Long scheduleId;
    private String comment;

    private String startedAt;
    private String endAt;

    private int participateCount; // 현재 일정 참여 인원
    private int totalMemberCount; // 스터디 전체 인원

    private boolean isParticipated; // 이 유저가 참여했는지 여부
}
