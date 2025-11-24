package com.example.gitrajabi.study.dto;

import com.example.gitrajabi.study.erum.StudyCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyCreateDto {
    private String studyName;
    private String studyDescription;
    private StudyCategory studyCategory;
    private int maxMembers;
}

