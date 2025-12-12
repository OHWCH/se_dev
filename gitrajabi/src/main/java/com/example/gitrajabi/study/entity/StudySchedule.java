package com.example.gitrajabi.study.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dev_study_schedule")
public class StudySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    private String comment;
    private LocalDateTime startedAt;
    private LocalDateTime endAt;

    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
