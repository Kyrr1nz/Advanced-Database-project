package com.example.student_management.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long class_id;

    private String className;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @OneToMany(mappedBy = "clazz", fetch = FetchType.EAGER)
    private Set<Student> students;

    // Getters and Setters
    public Long getClass_id() { return class_id; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public Major getMajor() { return major; }
    public void setMajor(Major major) { this.major = major; }
    public Set<Student> getStudents() { return students; }

}