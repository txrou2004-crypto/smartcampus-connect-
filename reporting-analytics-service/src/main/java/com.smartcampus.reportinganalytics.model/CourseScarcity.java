package com.smartcampus.reportinganalytics.model;

public class CourseScarcity {
    private String courseCode;
    private String courseName;
    private int fillRatePercentage;
    private String status; // "Filling Fast!" atau "Available"

    public CourseScarcity() {}

    public CourseScarcity(String courseCode, String courseName, int fillRatePercentage, String status) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.fillRatePercentage = fillRatePercentage;
        this.status = status;
    }

    // Getters and Setters
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public int getFillRatePercentage() { return fillRatePercentage; }
    public void setFillRatePercentage(int fillRatePercentage) { this.fillRatePercentage = fillRatePercentage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
