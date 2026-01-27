package com.example.student_management.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subjec_name", nullable = false)
    private String subjectName;

    private Integer credits;

    @OneToMany(mappedBy = "subject")
    private List<com.example.student_management.entity.CourseSection> courseSections;

    // Getter & Setter
    public Long getId() { return id; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
}