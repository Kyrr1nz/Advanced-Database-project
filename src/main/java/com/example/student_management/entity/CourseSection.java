package com.example.student_management.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;

@Entity
@Table(name = "course_sections")
public class CourseSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String semester;
    private Integer course_year;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "courseSection")
    private Set<Exam> exams;

    @OneToMany(mappedBy = "courseSection", fetch = FetchType.EAGER)
    private Set<Enrollment> enrollments = new HashSet<>();

    // Getter & Setter
    public Long getId() { return id; }
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }
    public Set<Exam> getExams() { return exams; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setSemester(String semester) {this.semester = semester; }
    public String getSemester() {return semester; }
    public void setCourse_year(Integer course_year) {this.course_year = course_year; }
    public Set<Enrollment> getEnrollments() { return enrollments; }

}