package com.example.gitrajabi.study.controller;

import com.example.gitrajabi.study.dto.StudyApplicantResponse;
import com.example.gitrajabi.study.dto.StudyMemberApplyRequest;
import com.example.gitrajabi.study.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studies/members")
@RequiredArgsConstructor
public class StudyMemberController {

    private final StudyMemberService studyMemberService;

    // 스터디 가입 신청
    @PostMapping("/apply")
    public ResponseEntity<String> applyStudy(@RequestBody StudyMemberApplyRequest request) {


        Long testUserId = 2L;

        studyMemberService.applyToStudy(request.getStudyId(), testUserId);

        return ResponseEntity.ok("스터디 가입 신청 완료");
    }

    // 스터디 가입 신청 목록 조회
    @GetMapping("/{studyId}/applicants")
    public ResponseEntity<List<StudyApplicantResponse>> getApplicants(
            @PathVariable Long studyId
    ) {
        List<StudyApplicantResponse> applicants = studyMemberService.getApplicants(studyId);
        return ResponseEntity.ok(applicants);
    }


    // 가입 수락
    @PostMapping("/{studyId}/approve/{userId}")
    public ResponseEntity<String> approveMember(
            @PathVariable Long studyId,
            @PathVariable Long userId
    ) {
        studyMemberService.approveMember(studyId, userId);
        return ResponseEntity.ok("가입 승인 완료");
    }

    // 가입 거절
    @PostMapping("/{studyId}/reject/{userId}")
    public ResponseEntity<String> rejectMember(
            @PathVariable Long studyId,
            @PathVariable Long userId
    ) {
        studyMemberService.rejectMember(studyId, userId);
        return ResponseEntity.ok("가입 거절 완료");
    }

}
