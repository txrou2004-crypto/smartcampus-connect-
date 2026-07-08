package com.smartcampus.reportinganalytics.model;

public class AcademicProgress {
    private String studentId;
    private int progressPercentage;

    public AcademicProgress() {}

    public AcademicProgress(String studentId, int progressPercentage) {
        this.studentId = studentId;
        this.progressPercentage = progressPercentage;
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public int getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(int progressPercentage) { this.progressPercentage = progressPercentage; }
}
