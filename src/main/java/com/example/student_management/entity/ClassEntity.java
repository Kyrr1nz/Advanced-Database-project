package com.example.student_management.entity;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "classes")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "course_year")
    private Integer courseYear;

    // 🔗 class → manager (N:1)
    @ManyToOne
    @JoinColumn(name = "mid")
    private Manager manager;

    // 🔗 class ↔ subject (N:N) qua bảng have
    @ManyToMany
    @JoinTable(
            name = "have",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects = new HashSet<>();

    // ===== Constructors =====
    protected ClassEntity() {
    }

    public ClassEntity(String className, Integer courseYear) {
        this.className = className;
        this.courseYear = courseYear;
    }

    // ===== Getter & Setter =====
    public Long getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(Integer courseYear) {
        this.courseYear = courseYear;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }
}
