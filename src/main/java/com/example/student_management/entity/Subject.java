package com.example.student_management.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    @Column(nullable = false, length = 100)
    private String subjectName;

    private Integer credits;
    private LocalDate sStartDate;
    private LocalDate sEndDate;

    @Column(length = 50)
    private String sType;

    @ManyToOne
    @JoinColumn(name = "sid")
    private Student student;

    // Constructors
    public Subject() {}

    // Getter & Setter
    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public LocalDate getsStartDate() {
        return sStartDate;
    }

    public void setsStartDate(LocalDate sStartDate) {
        this.sStartDate = sStartDate;
    }

    public LocalDate getsEndDate() {
        return sEndDate;
    }

    public void setsEndDate(LocalDate sEndDate) {
        this.sEndDate = sEndDate;
    }

    public String getsType() {
        return sType;
    }

    public void setsType(String sType) {
        this.sType = sType;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
