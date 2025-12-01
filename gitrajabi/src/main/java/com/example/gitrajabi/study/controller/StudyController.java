package com.example.gitrajabi.study.controller;


import com.example.gitrajabi.study.dto.*;
import com.example.gitrajabi.study.service.StudyService;
import com.example.gitrajabi.user.security.SecurityUtil;
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

        Long userId = SecurityUtil.getCurrentUserId(); // 로그인한 유저 id

        Long studyId = studyService.createStudy(request, userId);
        return ResponseEntity.ok(new MessageResponse("스터디 생성 완료. id = " + studyId));
    }

    // 전체 스터디 목록 조회 (로그인 유저의 가입 여부 포함)
    @GetMapping
    public ResponseEntity<List<StudyListResponse>> getStudyList() {

        Long userId = SecurityUtil.getCurrentUserId();

        List<StudyListResponse> list = studyService.getStudyList(userId);

        return ResponseEntity.ok(list);
    }

    // 내가 가입한 스터디 목록 조회
    @GetMapping("/me")
    public ResponseEntity<List<StudyListResponse>> getMyStudyList() {

        Long userId = SecurityUtil.getCurrentUserId();

        List<StudyListResponse> list = studyService.getMyStudyList(userId);

        return ResponseEntity.ok(list);
    }

    // 스터디 관리 정보 조회 (스터디장만 조회 가능)
    @GetMapping("/{studyId}/manage")
    public ResponseEntity<StudyManageResponse> getManagePageInfo(
            @PathVariable Long studyId
    ) {
        Long userId = SecurityUtil.getCurrentUserId();

        StudyManageResponse response = studyService.getManagePageInfo(studyId, userId);

        return ResponseEntity.ok(response);
    }

    // 스터디 정보 수정
    @PutMapping("/{studyId}")
    public ResponseEntity<MessageResponse> updateStudy(
            @PathVariable Long studyId,
            @RequestBody StudyUpdateDto request
    ) {
        Long userId = SecurityUtil.getCurrentUserId();

        studyService.updateStudy(studyId, userId, request);

        return ResponseEntity.ok(new MessageResponse("스터디 정보가 수정되었습니다."));
    }

    // 스터디 메인 페이지 조회
    @GetMapping("/{studyId}/main")
    public ResponseEntity<StudyMainPageResponse> getStudyMainPage(
            @PathVariable Long studyId
    ) {
        StudyMainPageResponse response = studyService.getStudyMainPage(studyId);
        return ResponseEntity.ok(response);
    }


}
