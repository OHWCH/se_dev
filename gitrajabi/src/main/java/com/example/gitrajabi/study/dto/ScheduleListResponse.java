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

    private int participateCount;
    private int totalMemberCount;

    private boolean isParticipated;
}
