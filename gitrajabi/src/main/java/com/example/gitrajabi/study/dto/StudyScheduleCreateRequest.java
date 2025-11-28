package com.example.gitrajabi.study.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StudyScheduleCreateRequest {

    private String comment;
    private LocalDateTime startedAt;
    private LocalDateTime endAt;

}
