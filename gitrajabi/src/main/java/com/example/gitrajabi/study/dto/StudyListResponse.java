package com.example.gitrajabi.study.dto;



import com.example.gitrajabi.study.erum.JoinStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class StudyListResponse {

    private Long studyId;
    private String name;
    private String description;

    private int currentMembers;
    private int maxMembers;

    private JoinStatus userJoinStatus;
}
