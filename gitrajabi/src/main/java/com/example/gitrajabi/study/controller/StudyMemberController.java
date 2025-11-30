package com.example.gitrajabi.study.controller;


import com.example.gitrajabi.study.dto.MessageResponse;
import com.example.gitrajabi.study.dto.StudyMemberResponse;
import com.example.gitrajabi.study.service.StudyMemberService;
import com.example.gitrajabi.user.security.SecurityUtil;
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
    public ResponseEntity<MessageResponse> applyStudy(@PathVariable Long studyId) {

        Long userId = SecurityUtil.getCurrentUserId();

        studyMemberService.applyToStudy(studyId, userId);

        return ResponseEntity.ok(new MessageResponse("스터디 가입 신청 완료"));
    }

    // 가입 수락 (리더만 가능)
    @PostMapping("/{studyId}/approve/{userId}")
    public ResponseEntity<MessageResponse> approveMember(
            @PathVariable Long studyId,
            @PathVariable Long userId
    ) {
        studyMemberService.approveMember(studyId, userId);

        return ResponseEntity.ok(new MessageResponse("가입 승인 완료"));
    }

    // 가입 거절
    @PostMapping("/{studyId}/reject/{userId}")
    public ResponseEntity<MessageResponse> rejectMember(
            @PathVariable Long studyId,
            @PathVariable Long userId
    ) {
        studyMemberService.rejectMember(studyId, userId);

        return ResponseEntity.ok(new MessageResponse("가입 거절 완료"));
    }

    // 스터디 멤버 리스트 조회
    @GetMapping("/{studyId}/members")
    public ResponseEntity<List<StudyMemberResponse>> getStudyMembers(
            @PathVariable Long studyId
    ) {
        List<StudyMemberResponse> members = studyMemberService.getStudyMembers(studyId);
        return ResponseEntity.ok(members);
    }

    // 스터디 탈퇴 (본인만)
    @DeleteMapping("/{studyId}/members/me")
    public ResponseEntity<MessageResponse> leaveStudy(
            @PathVariable Long studyId
    ) {
        Long userId = SecurityUtil.getCurrentUserId();

        studyMemberService.leaveStudy(studyId, userId);

        return ResponseEntity.ok(new MessageResponse("스터디 탈퇴가 완료되었습니다."));
    }

    // 스터디 강퇴 (리더만)
    @DeleteMapping("/{studyId}/members/{targetUserId}")
    public ResponseEntity<MessageResponse> kickMember(
            @PathVariable Long studyId,
            @PathVariable Long targetUserId
    ) {
        Long leaderId = SecurityUtil.getCurrentUserId(); // 로그인한 유저(리더)

        studyMemberService.kickMember(studyId, leaderId, targetUserId);

        return ResponseEntity.ok(new MessageResponse("강퇴가 완료되었습니다."));
    }

}
