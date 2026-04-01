package com.example.student_management.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "course_section")
public class CourseSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long course_section_id;

    private String semester;
    private Integer course_year;

    private LocalDate startDate;
    private LocalDate endDate;
    private String sectionName;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "courseSection", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Enrollment> enrollments = new HashSet<>();

    @OneToMany(mappedBy = "courseSection", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Exam> exams;

    // Getter & Setter
    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
    public Long getCourse_section_id() { return course_section_id; }
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
    public Integer getCourse_year() {return course_year;}
    public Set<Enrollment> getEnrollments() { return enrollments; }
    @Override
    public String toString() {
        String subjectName = (subject != null) ? subject.getSubjectName() : "N/A";
        return subjectName + " - Lớp " + course_section_id;
    }
}