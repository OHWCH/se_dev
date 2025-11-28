package com.example.gitrajabi.study.entity;



import com.example.gitrajabi.study.erum.JoinStatus;
import com.example.gitrajabi.study.erum.StudyRole;
import com.example.gitrajabi.test_user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "dev_study_members",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_study_member_study_user",
                        columnNames = {"study_id", "user_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StudyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_member_id")
    private Long studyMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StudyRole studyRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JoinStatus joinStatus;

}
