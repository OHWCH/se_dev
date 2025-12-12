package com.example.gitrajabi.study.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudyManageResponse {
    private StudyInfoResponse studyInfo;  // 스터디 현재 정보
    private List<StudyMemberResponse> members; // 스터디 멤버 목록
    private List<StudyApplicantResponse> applicants; // 가입 신청 목록

}
