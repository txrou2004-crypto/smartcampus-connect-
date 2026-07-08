package com.smartcampus.studentservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "academic_records")
public class AcademicRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    @JsonProperty("record_id")
    private Integer recordId;

    @NotBlank(message = "Academic year is mandatory")
    @Column(name = "academic_year", nullable = false, length = 9)
    @JsonProperty("academic_year")
    private String academicYear; // e.g. "2024/2025"

    @Min(value = 1, message = "Semester must be between 1 and 3")
    @Max(value = 3, message = "Semester must be between 1 and 3")
    @Column(nullable = false)
    private int semester;

    @DecimalMin(value = "0.00", message = "GPA cannot be negative")
    @DecimalMax(value = "4.00", message = "GPA cannot exceed 4.00")
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal gpa;

    @DecimalMin(value = "0.00", message = "CGPA cannot be negative")
    @DecimalMax(value = "4.00", message = "CGPA cannot exceed 4.00")
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal cgpa;

    @Min(value = 0, message = "Credits earned cannot be negative")
    @Column(name = "credits_earned", nullable = false)
    @JsonProperty("credits_earned")
    private int creditsEarned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties("academicRecords")  
    @JsonBackReference
    private Student student;

    public AcademicRecord() {}

    // Getters and Setters
    public Integer getRecordId() { return recordId; }
    public void setRecordId(Integer recordId) { this.recordId = recordId; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public BigDecimal getGpa() { return gpa; }
    public void setGpa(BigDecimal gpa) { this.gpa = gpa; }

    public BigDecimal getCgpa() { return cgpa; }
    public void setCgpa(BigDecimal cgpa) { this.cgpa = cgpa; }

    public int getCreditsEarned() { return creditsEarned; }
    public void setCreditsEarned(int creditsEarned) { this.creditsEarned = creditsEarned; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}
