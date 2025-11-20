package com.example.gitrajabi.study.controller;


import com.example.gitrajabi.study.dto.StudyCreateDto;
import com.example.gitrajabi.study.dto.StudyListResponse;
import com.example.gitrajabi.study.dto.StudyManageResponse;
import com.example.gitrajabi.study.dto.StudyUpdateDto;
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

        // 스프링 시큐리티를 아직 안 써서 임시로 로그인 유저 ID를 하드코딩
        Long testUserId = 1L;

        Long studyId = studyService.createStudy(request, testUserId);
        return ResponseEntity.ok(studyId);
    }


    // 스터디 리스트 반환
    @GetMapping
    public List<StudyListResponse> getStudyList() {

        Long testUserId = 2L;
        return studyService.getStudyList(testUserId);


    }

/*  시큐리티 활성화시 리스트 반환 코드
    @GetMapping
    public List<StudyListResponse> getStudyList(@AuthenticationPrincipal CustomUserDetails user) {
        return studyService.getStudyList(user.getId());
    }*/

    // 스터디 관리
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
    public ResponseEntity<String> updateStudy(
            @PathVariable Long studyId,
            @RequestBody StudyUpdateDto request
    ) {
        Long userId = 1L; // ⭐ JWT 적용 전 임시 값

        studyService.updateStudy(studyId, userId, request);
        return ResponseEntity.ok("스터디 정보가 수정되었습니다.");
    }

}
