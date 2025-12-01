package com.example.gitrajabi.study.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudyMainPageResponse {

    private Long studyId;
    private String studyName;
    private String studyDescription;
    private String studyCategory;

    private int currentMembers;
    private int maxMembers;

    private String leaderGithubId;

    private List<StudyMemberResponse> members;
    private List<StudyMainScheduleResponse> schedules;
}
