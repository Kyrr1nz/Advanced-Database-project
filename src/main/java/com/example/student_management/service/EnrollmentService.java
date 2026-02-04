package com.example.student_management.service;

import com.example.student_management.entity.Enrollment;
import com.example.student_management.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Enrollment> findByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getFullStudentDetails(Long studentId) {
        return enrollmentRepository.findFullDetailsByStudentId(studentId);
    }

    public Enrollment save(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    public void delete(Long enrollmentId) {
        enrollmentRepository.deleteById(enrollmentId);
    }
}