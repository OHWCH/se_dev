package com.example.gitrajabi.study.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyStudyListResponse {

    private Long studyId;
    private String name;
    private String description;
    private int currentMembers;
    private int maxMembers;
    private String studyRole;   // LEADER / MEMBER
}
