package com.example.gitrajabi.study.entity;



import com.example.gitrajabi.test_user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dev_schedule_participate")
public class ScheduleParticipate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private StudySchedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
