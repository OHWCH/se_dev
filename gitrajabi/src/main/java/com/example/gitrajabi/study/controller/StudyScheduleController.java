package com.example.gitrajabi.study.controller;


import com.example.gitrajabi.study.dto.MessageResponse;
import com.example.gitrajabi.study.dto.ScheduleListResponse;
import com.example.gitrajabi.study.dto.StudyScheduleCreateRequest;
import com.example.gitrajabi.study.service.StudyScheduleService;
import com.example.gitrajabi.user.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studies/{studyId}/schedules")
@RequiredArgsConstructor
public class StudyScheduleController {

    private final StudyScheduleService studyScheduleService;

    // 🔹 스터디 일정 생성
    @PostMapping
    public ResponseEntity<MessageResponse> createSchedule(
            @PathVariable Long studyId,
            @RequestBody StudyScheduleCreateRequest request
    ) {
        Long userId = SecurityUtil.getCurrentUserId();

        studyScheduleService.createSchedule(studyId, userId, request);

        return ResponseEntity.ok(new MessageResponse("스터디 일정 생성 완료"));
    }

    // 🔹 일정 참여
    @PostMapping("/{scheduleId}/participate")
    public ResponseEntity<MessageResponse> participate(
            @PathVariable Long studyId,
            @PathVariable Long scheduleId
    ) {
        Long userId = SecurityUtil.getCurrentUserId();

        studyScheduleService.participate(studyId, scheduleId, userId);

        return ResponseEntity.ok(new MessageResponse("일정 참석이 완료되었습니다."));
    }

    // 🔹 일정 목록 조회
    @GetMapping
    public ResponseEntity<List<ScheduleListResponse>> getSchedules(
            @PathVariable Long studyId
    ) {
        Long userId = SecurityUtil.getCurrentUserId();

        List<ScheduleListResponse> result =
                studyScheduleService.getScheduleList(studyId, userId);

        return ResponseEntity.ok(result);
    }
}
