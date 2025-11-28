package com.example.gitrajabi.study.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudyManageResponse {
    private StudyInfoResponse studyInfo;  // 스터디 현재 정보
    private List<StudyApplicantResponse> applicants; // 가입 신청 목록
}
