package com.example.gitrajabi.study.controller;


import com.example.gitrajabi.study.dto.StudyCreateDto;
import com.example.gitrajabi.study.dto.StudyListResponse;
import com.example.gitrajabi.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    // 스터디 생성
    @PostMapping
    public ResponseEntity<Long> createStudy(@RequestBody StudyCreateDto request) {

        Long studyId = studyService.createStudy(request);
        return ResponseEntity.ok(studyId);
    }

    // 스터디 삭제
    @GetMapping
    public List<StudyListResponse> getStudyList(@RequestParam Long userId) {
        return studyService.getStudyList(userId);
    }
}
