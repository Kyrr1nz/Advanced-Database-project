package com.example.student_management.entity;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subject_id;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    private Integer credits;

    @OneToMany(mappedBy = "subject",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<com.example.student_management.entity.CourseSection> courseSections;

    // Getter & Setter
    public Long getSubject_id() { return subject_id; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

}