package com.example.gitrajabi.study.repository;

import com.example.gitrajabi.study.entity.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {
    List<StudySchedule> findByStudy_StudyId(Long studyId);

}
