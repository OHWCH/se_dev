package com.example.gitrajabi.study.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyMainScheduleResponse {
    private Long scheduleId;
    private String comment;
    private String startedAt;
    private String endAt;
}
