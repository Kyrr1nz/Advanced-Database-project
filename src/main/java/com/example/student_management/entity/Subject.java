package com.example.student_management.entity;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;

@Entity
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "credit")
    private Integer credit;

    // =========================
    // Student <-> Subject (N-N)
    // inverse side of SIGN
    // =========================
    @ManyToMany(mappedBy = "subjects")
    private Set<Student> students = new HashSet<>();

    // =========================
    // Class <-> Subject (N-N)
    // join table: HAVE
    // =========================
    @ManyToMany
    @JoinTable(
            name = "have",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private Set<ClassEntity> classes = new HashSet<>();

    // ===== Constructors =====
    protected Subject() {}

    public Subject(String subjectName, Integer credit) {
        this.subjectName = subjectName;
        this.credit = credit;
    }

    // ===== Getter & Setter =====
    public Long getId() {
        return id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Set<ClassEntity> getClasses() {
        return classes;
    }
}
