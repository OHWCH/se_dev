package com.example.gitrajabi.study.dto;

import com.example.gitrajabi.study.erum.StudyCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyInfoResponse {
    private Long studyId;
    private String studyName;
    private String studyDescription;
    private StudyCategory studyCategory;
    private int maxMemberCount;
}
