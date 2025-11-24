package com.example.gitrajabi.study.controller;

import com.example.gitrajabi.study.dto.StudyApplicantResponse;
import com.example.gitrajabi.study.dto.StudyManageResponse;
import com.example.gitrajabi.study.dto.StudyMemberResponse;
import com.example.gitrajabi.study.service.StudyMemberService;
import com.example.gitrajabi.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyMemberController {
    private final StudyMemberService studyMemberService;
    // 스터디 가입 신청
    @PostMapping("/{studyId}/apply")
    public ResponseEntity<String> applyStudy(@PathVariable Long studyId) {
        Long testUserId = 5L;
        studyMemberService.applyToStudy(studyId, testUserId);
        return ResponseEntity.ok("스터디 가입 신청 완료");
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

    // 스터디 멤버 리스트
    @GetMapping("/{studyId}/members")
    public ResponseEntity<List<StudyMemberResponse>> getStudyMembers(
            @PathVariable Long studyId
    ) {
        List<StudyMemberResponse> members = studyMemberService.getStudyMembers(studyId);
        return ResponseEntity.ok(members);
    }

    // 스터디 탈퇴
    @DeleteMapping("/{studyId}/members/me")
    public ResponseEntity<String> leaveStudy(
            @PathVariable Long studyId
    ) {
        Long userId = 1L;

        studyMemberService.leaveStudy(studyId, userId);

        return ResponseEntity.ok("스터디 탈퇴가 완료되었습니다.");
    }

}
