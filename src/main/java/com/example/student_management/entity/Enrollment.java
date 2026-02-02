package com.example.student_management.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "section_id"})
)
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private com.example.student_management.entity.CourseSection courseSection;

    private LocalDate EnrollmentDate;
    private String status;

    public Enrollment() {}

    public Enrollment(Student student, com.example.student_management.entity.CourseSection courseSection) {
        this.student = student;
        this.courseSection = courseSection;
        this.status = "ENROLLED";
        if (courseSection != null && courseSection.getStartDate() != null) {
            this.EnrollmentDate = courseSection.getStartDate().minusWeeks(3);
        }
    }

    public Long getEnrollmentId() { return enrollmentId; }
    public void setCourseSection(CourseSection courseSection) {this.courseSection = courseSection;}
    public void setStudent(Student student) {this.student = student;}
    public Student getStudent() { return student; }
    public com.example.student_management.entity.CourseSection getCourseSection() { return courseSection; }
    public LocalDate getEnrollmentDate() { return EnrollmentDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public void setEnrollmentDate(LocalDate registerDate) {this.EnrollmentDate =registerDate;}
}
