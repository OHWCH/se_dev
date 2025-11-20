package com.example.gitrajabi.study.dto;

import com.example.gitrajabi.study.erum.JoinStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyApplicantResponse {
    private Long userId;
    private String nickname;
    private JoinStatus joinStatus;
}
