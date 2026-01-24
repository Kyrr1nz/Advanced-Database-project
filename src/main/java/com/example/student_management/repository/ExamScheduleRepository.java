package com.example.student_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.student_management.entity.ExamSchedule;

public interface ExamScheduleRepository extends JpaRepository<ExamSchedule, Long> {
}
