package com.example.student_management.service;

import com.example.student_management.entity.Student;
import com.example.student_management.entity.Enrollment;
import com.example.student_management.repository.StudentRepository;
import com.example.student_management.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // Lấy tổng số sinh viên cho màn hình Dashboard
    public long countStudents() {
        return studentRepository.count();
    }

    // Lấy danh sách kèm thông tin Lớp/Ngành cho Layer 1
    public List<Student> findAllForList() {
        return studentRepository.findAllWithBasicInfo();
    }

    // Lấy hồ sơ chi tiết (Môn học, Giảng viên, Lịch thi...) cho Layer 2
    public List<Enrollment> getStudentFullProfile(Long studentId) {
        return enrollmentRepository.findFullDetailsByStudentId(studentId);
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public void save(Student student) {
        // Trước khi lưu, bạn có thể xử lý logic tự động cộng MSSV tại đây
        studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

}