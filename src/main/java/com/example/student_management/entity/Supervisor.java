package com.example.student_management.entity;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "supervisor")
public class Supervisor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;

    @Column(name = "sname")
    private String sName;

    @Column(name = "snumber")
    private String sNumber;

    @Column(name = "ssubject")
    private String sSubject;

    // Mối quan hệ Become: 1 Teacher có thể là 1 Supervisor
    @OneToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    // Mối quan hệ Manage: 1 Supervisor quản lý nhiều Exam
    @OneToMany(mappedBy = "supervisor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Exam> exams;

    // Getters and Setters
    public Long getSid() { return sid; }
    public String getsName() { return sName; }
    public void setsName(String sName) { this.sName = sName; }
    public String getsNumber() { return sNumber; }
    public void setsNumber(String sNumber) { this.sNumber = sNumber; }
    public String getsSubject() { return sSubject; }
    public void setsSubject(String sSubject) { this.sSubject = sSubject; }
    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }
}