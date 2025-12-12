package com.example.gitrajabi.study.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudyPageResponse {

    private List<StudyListResponse> content;  // 현재 페이지 데이터
    private int currentPage;                 // 현재 페이지 번호
    private int totalPages;                  // 총 페이지 수
    private long totalElements;              // 전체 스터디 개수
}
