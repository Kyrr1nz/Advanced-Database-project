package com.example.student_management.repository;

import com.example.student_management.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);
    // Truy vấn lấy toàn bộ thông tin Môn học, Giáo viên, Lịch học, Lịch thi của 1 sinh viên
    @Query("SELECT e FROM Enrollment e " +
            "JOIN FETCH e.courseSection cs " +
            "JOIN FETCH cs.subject " +
            "JOIN FETCH cs.teacher " +
            "LEFT JOIN FETCH cs.exams " +
            "WHERE e.student.id = :studentId")
    List<Enrollment> findFullDetailsByStudentId(@Param("studentId") Long studentId);
}