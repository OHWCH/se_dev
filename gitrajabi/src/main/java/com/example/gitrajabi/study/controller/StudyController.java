package com.example.gitrajabi.study.controller;


import com.example.gitrajabi.study.dto.*;
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
    public ResponseEntity<MessageResponse> createStudy(@RequestBody StudyCreateDto request) {

        Long testUserId = 2L; // 임시 로그인 유저

        Long studyId = studyService.createStudy(request, testUserId);
        return ResponseEntity.ok(new MessageResponse("스터디 생성 완료. id = " + studyId));
    }
    // 스터디 리스트 반환
    @GetMapping
    public ResponseEntity<List<StudyListResponse>> getStudyList() {

        Long testUserId = 1L;

        List<StudyListResponse> list = studyService.getStudyList(testUserId);

        return ResponseEntity.ok(list);
    }


    // 내 스터디 리스트 반환
    @GetMapping("/me")
    public ResponseEntity<List<StudyListResponse>> getMyStudyList() {

        Long testUserId = 1L;

        List<StudyListResponse> list = studyService.getMyStudyList(testUserId);

        return ResponseEntity.ok(list);
    }





    // 스터디 관리 정보 조회
    @GetMapping("/{studyId}/manage")
    public ResponseEntity<StudyManageResponse> getManagePageInfo(
            @PathVariable Long studyId
    ) {
        Long userId = 1L;
        StudyManageResponse response = studyService.getManagePageInfo(studyId, userId);

        return ResponseEntity.ok(response);
    }

    // 스터디 정보 수정
    @PutMapping("/{studyId}")
    public ResponseEntity<MessageResponse> updateStudy(
            @PathVariable Long studyId,
            @RequestBody StudyUpdateDto request
    ) {
        Long userId = 1L;

        studyService.updateStudy(studyId, userId, request);

        return ResponseEntity.ok(new MessageResponse("스터디 정보가 수정되었습니다."));
    }

}
