package com.example.gitrajabi.study.controller;


import com.example.gitrajabi.study.dto.StudyScheduleCreateRequest;
import com.example.gitrajabi.study.service.StudyScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studies/{studyId}/schedules")
@RequiredArgsConstructor
public class StudyScheduleController {

    private final StudyScheduleService studyScheduleService;

    // 스터디 일정 생성
    @PostMapping
    public ResponseEntity<String> createSchedule(@PathVariable Long studyId, @RequestBody StudyScheduleCreateRequest request
    ) {
        Long testUserId = 2L;
        studyScheduleService.createSchedule(studyId, testUserId, request);
        return ResponseEntity.ok("스터디 일정 생성 완료");
    }
}
