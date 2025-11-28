package com.example.gitrajabi.study.dto;



import com.example.gitrajabi.study.erum.JoinStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyMemberResponse {

    private Long userId;
    private String githubId;
    private JoinStatus joinStatus;
    private String studyRole;
}
