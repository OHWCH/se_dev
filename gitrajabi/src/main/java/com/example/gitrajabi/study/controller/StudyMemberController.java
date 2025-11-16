package com.example.gitrajabi.study.controller;

import com.example.gitrajabi.study.dto.StudyMemberApplyRequest;
import com.example.gitrajabi.study.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studies/members")
@RequiredArgsConstructor
public class StudyMemberController {

    private final StudyMemberService studyMemberService;

    // 스터디 가입 신청
    @PostMapping("/apply")
    public ResponseEntity<String> applyStudy(@RequestBody StudyMemberApplyRequest request) {

        // 스프링 시큐리티 비활성화 상태 → 임시 로그인 유저 ID
        Long testUserId = 1L;

        studyMemberService.applyToStudy(request.getStudyId(), testUserId);

        return ResponseEntity.ok("스터디 가입 신청 완료");
    }
}
