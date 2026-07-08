package com.smartcampus.reportinganalytics.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationDTO {

    private Long roomReservationId;
    private String studentId;
    private String reservationDate;
    private String status;

    public Long getRoomReservationId() {
        return roomReservationId;
    }

    public void setRoomReservationId(Long roomReservationId) {
        this.roomReservationId = roomReservationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
