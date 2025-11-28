package com.example.gitrajabi.study.repository;



import com.example.gitrajabi.study.entity.ScheduleParticipate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleParticipateRepository extends JpaRepository<ScheduleParticipate, Long> {
    boolean existsBySchedule_ScheduleIdAndUser_Id(Long scheduleId, Long userId);
    int countBySchedule_ScheduleId(Long scheduleId);


}
