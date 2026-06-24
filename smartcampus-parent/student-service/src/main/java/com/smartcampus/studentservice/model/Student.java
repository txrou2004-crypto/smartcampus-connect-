package com.smartcampus.studentservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {
	@Id
    @Column(name = "student_id", length = 10)
    @NotBlank(message = "Student ID cannot be empty")
    @Size(max = 10, message = "Student ID must be at most 10 characters")
    @JsonProperty("student_id")
    private String studentId;

    @NotBlank(message = "First name is mandatory")
    @Column(name = "first_name", nullable = false, length = 50)
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Column(name = "last_name", nullable = false, length = 50)
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "Email field cannot be blank")
    @Email(message = "Must provide a well-formed campus email address")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Telephone number is mandatory")
    @Column(name = "tel_number", nullable = false, length = 20)
    @JsonProperty("tel_number")
    private String telNumber;

    @NotBlank(message = "Programme code is mandatory")
    @Column(name = "programme_code", nullable = false, length = 20)
    @JsonProperty("programme_code")
    private String programmeCode;

    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "Status must be ACTIVE or INACTIVE")
    @Column(nullable = false)
    private String status = "ACTIVE";

    @Valid
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("academic_records")
    private List<AcademicRecord> academicRecords = new ArrayList<>();

    public Student() {}

    public void addAcademicRecord(AcademicRecord record) {
        academicRecords.add(record);
        record.setStudent(this);
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelNumber() { return telNumber; }
    public void setTelNumber(String telNumber) { this.telNumber = telNumber; }

    public String getProgrammeCode() { return programmeCode; }
    public void setProgrammeCode(String programmeCode) { this.programmeCode = programmeCode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<AcademicRecord> getAcademicRecords() { return academicRecords; }
    public void setAcademicRecords(List<AcademicRecord> academicRecords) { this.academicRecords = academicRecords; }
}
