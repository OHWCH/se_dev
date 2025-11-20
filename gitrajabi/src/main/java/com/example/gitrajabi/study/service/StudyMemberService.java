package com.example.gitrajabi.study.service;

import com.example.gitrajabi.study.dto.StudyApplicantResponse;
import com.example.gitrajabi.study.dto.StudyMemberResponse;
import com.example.gitrajabi.study.entity.Study;
import com.example.gitrajabi.study.entity.StudyMember;
import com.example.gitrajabi.study.entity.User;
import com.example.gitrajabi.study.erum.JoinStatus;
import com.example.gitrajabi.study.erum.StudyRole;
import com.example.gitrajabi.study.repository.StudyMemberRepository;
import com.example.gitrajabi.study.repository.StudyRepository;
import com.example.gitrajabi.study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyMemberService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final UserRepository userRepository;


    public void applyToStudy(Long studyId, Long userId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        boolean exists = studyMemberRepository
                .existsByStudy_StudyIdAndUser_Id(studyId, userId);

        if (exists) {
            throw new IllegalStateException("이미 가입했거나 신청한 스터디입니다.");
        }
        StudyMember member = StudyMember.builder()
                .study(study)
                .user(user)
                .studyRole(StudyRole.MEMBER)
                .joinStatus(JoinStatus.APPLIED)
                .build();

        studyMemberRepository.save(member);
    }

    // 스터디 가입 신청 목록 조회

    public List<StudyApplicantResponse> getApplicants(Long studyId) {

        List<StudyMember> applicants =
                studyMemberRepository.findByStudy_StudyIdAndJoinStatus(studyId, JoinStatus.APPLIED);

        return applicants.stream()
                .map(member -> StudyApplicantResponse.builder()
                        .userId(member.getUser().getId())
                        .nickname(member.getUser().getNickname())
                        .joinStatus(member.getJoinStatus()) // APPLIED
                        .build())
                .toList();
    }

    // 가입 승인
    public void approveMember(Long studyId, Long userId) {


        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        StudyMember member = studyMemberRepository
                .findByStudy_StudyIdAndUser_Id(studyId, userId)
                .orElseThrow(() -> new IllegalArgumentException("가입 신청 내역이 없습니다."));

        if (member.getJoinStatus() != JoinStatus.APPLIED) {
            throw new IllegalStateException("이미 처리된 요청입니다.");
        }

        member.setJoinStatus(JoinStatus.APPROVED);
    }

    // 가입 거절
    @Transactional
    public void rejectMember(Long studyId, Long userId) {

        StudyMember member = studyMemberRepository
                .findByStudy_StudyIdAndUser_Id(studyId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 스터디에 가입 신청한 상태가 아닙니다."));

        member.setJoinStatus(JoinStatus.REJECTED);

        studyMemberRepository.save(member);
    }

    // 스터디 멤버 목록 조회
    public List<StudyMemberResponse> getStudyMembers(Long studyId) {

        List<StudyMember> members =
                studyMemberRepository.findByStudy_StudyIdAndJoinStatusNot(studyId, JoinStatus.REJECTED);


        return members.stream()
                .map(m -> StudyMemberResponse.builder()
                        .userId(m.getUser().getId())
                        .nickname(m.getUser().getNickname())
                        .joinStatus(m.getJoinStatus())
                        .studyRole(m.getStudyRole().name())
                        .build()
                )
                .toList();
    }


}
