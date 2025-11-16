package com.example.gitrajabi.study.controller;


import com.example.gitrajabi.study.dto.StudyCreateDto;
import com.example.gitrajabi.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    // ✔ 스터디 생성 API
    @PostMapping
    public ResponseEntity<Long> createStudy(@RequestBody StudyCreateDto request) {

        Long studyId = studyService.createStudy(request);

        // 생성된 스터디 ID 반환
        return ResponseEntity.ok(studyId);
    }
}
