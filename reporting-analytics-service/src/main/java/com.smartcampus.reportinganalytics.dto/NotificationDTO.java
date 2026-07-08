package com.smartcampus.reportinganalytics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationDTO {

    @JsonProperty("notification_id")
    private Long id;

    @JsonProperty("student_id")
    private String studentId;

    @JsonProperty("notification_type")
    private String type; // Maps "EMAIL", "SMS", "PUSH" cleanly

    @JsonProperty("sent_time")
    private String sentTime;

    // Keep all your existing Getters and Setters exactly as they are!
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSentTime() { return sentTime; }
    public void setSentTime(String sentTime) { this.sentTime = sentTime; }
}
