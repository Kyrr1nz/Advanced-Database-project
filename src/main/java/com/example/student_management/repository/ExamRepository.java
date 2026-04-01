package com.example.student_management.repository;

import com.example.student_management.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT e FROM Exam e " +
            "LEFT JOIN FETCH e.courseSection cs " +
            "LEFT JOIN FETCH cs.subject " +
            "LEFT JOIN FETCH cs.enrollments en " +
            "LEFT JOIN FETCH en.student " +
            "LEFT JOIN FETCH e.supervisor " +
            "WHERE e.exam_id = :id")
    Optional<Exam> findByIdWithDetails(@Param("id") Long id);
}