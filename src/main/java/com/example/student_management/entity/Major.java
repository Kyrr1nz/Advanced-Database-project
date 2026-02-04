package com.example.student_management.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "major")
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long major_id;

    @Column(name = "major_name", nullable = false)
    private String majorName;

    // Một ngành có nhiều lớp
    @OneToMany(mappedBy = "major")
    private List<com.example.student_management.entity.ClassEntity> classes;

    public Major() {}

    // Getter & Setter
    public Long getId() { return major_id; }
    public void setId(Long id) { this.major_id = id; }

    public String getMajorName() { return majorName; }
    public void setMajorName(String majorName) { this.majorName = majorName; }

    public List<com.example.student_management.entity.ClassEntity> getClasses() { return classes; }
    public void setClasses(List<com.example.student_management.entity.ClassEntity> classes) { this.classes = classes; }
}