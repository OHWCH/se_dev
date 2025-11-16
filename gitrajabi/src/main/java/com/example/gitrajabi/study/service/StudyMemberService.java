package com.example.gitrajabi.study.service;

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

@Service
@RequiredArgsConstructor
@Transactional
public class StudyMemberService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final UserRepository userRepository;

    // 스터디 가입 신청
    public void applyToStudy(Long studyId, Long userId) {

        // 1) 스터디 존재 확인
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));

        // 2) 유저 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        // 3) 이미 신청했거나 가입된 경우 체크
        boolean exists = studyMemberRepository
                .existsByStudy_StudyIdAndUser_Id(studyId, userId);

        if (exists) {
            throw new IllegalStateException("이미 가입했거나 신청한 스터디입니다.");
        }

        // 4) 신청 상태로 멤버 추가
        StudyMember member = StudyMember.builder()
                .study(study)
                .user(user)
                .studyRole(StudyRole.MEMBER)
                .joinStatus(JoinStatus.APPLIED)
                .build();

        studyMemberRepository.save(member);
    }
}
