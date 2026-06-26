package com.smartcampus.libraryservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @Column(name = "room_id")
    @NotBlank(message = "Room ID is required")
    private String roomId;

    @Column(name = "room_name")
    @NotBlank(message = "Room name is required")
    private String roomName;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    @Column(name = "availability_status")
    private String availabilityStatus = "AVAILABLE";

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
}