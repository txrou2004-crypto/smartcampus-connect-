package com.smartcampus.enrollmentservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @Column(name = "course_id", length = 10)
    private String courseId;

    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    @Min(1)
    @Column(name = "credit_hours", nullable = false)
    private int creditHours;

    @Min(10)
    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @Min(0)
    @Column(name = "available_seats", nullable = false)
    private int availableSeats;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
