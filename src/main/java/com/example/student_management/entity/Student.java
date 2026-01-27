package com.example.student_management.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String phoneNumber;
    private String gender;
    private LocalDate dob;

    @Column(unique = true)
    private String mssv;

    // Nối với Clazz để lấy tên lớp/ngành ở Layer 1
    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity clazz;

    // Nối với Enrollment để đi đến Subject/Teacher ở Layer 2
    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments;

    public Student() {
    }

    // Getters & Setters bắt buộc để UI không bị lỗi đỏ
    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public ClassEntity getClazz() {
        return clazz;
    }
    public String getMssv() {
        return mssv;
    }
    public void setMssv(String mssv){
        this.mssv = mssv;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public String getEmail() {
        return email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setClazz(ClassEntity clazz) {
        this.clazz = clazz;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}