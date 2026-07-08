package com.smartcampus.notification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notification_id;
    
    private String student_id;
    
    // Kept as Integer to match your current database schema
    private Integer enrolment_id; 
    
    // Added these fields to support different event types
    private Integer reservation_id;
    private Integer room_reservation_id;
    
    private String message;
    private String notification_type; 
    
    @Column(insertable = false, updatable = false)
    private LocalDateTime sent_time;
    
    private String status = "SENT";

    // --- Getters and Setters ---
    public Integer getNotification_id() { return notification_id; }
    public void setNotification_id(Integer notification_id) { this.notification_id = notification_id; }

    public String getStudent_id() { return student_id; }
    public void setStudent_id(String student_id) { this.student_id = student_id; }

    public Integer getEnrolment_id() { return enrolment_id; }
    public void setEnrolment_id(Integer enrolment_id) { this.enrolment_id = enrolment_id; }

    // New Getters and Setters
    public Integer getReservation_id() { return reservation_id; }
    public void setReservation_id(Integer reservation_id) { this.reservation_id = reservation_id; }

    public Integer getRoom_reservation_id() { return room_reservation_id; }
    public void setRoom_reservation_id(Integer room_reservation_id) { this.room_reservation_id = room_reservation_id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getNotification_type() { return notification_type; }
    public void setNotification_type(String notification_type) { this.notification_type = notification_type; }

    public LocalDateTime getSent_time() { return sent_time; }
    public void setSent_time(LocalDateTime sent_time) { this.sent_time = sent_time; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
