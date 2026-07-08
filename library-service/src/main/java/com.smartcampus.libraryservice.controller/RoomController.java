package com.smartcampus.libraryservice.controller;

import com.smartcampus.libraryservice.model.Room;
import com.smartcampus.libraryservice.model.RoomReservation;
import com.smartcampus.libraryservice.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    private final LibraryService service;

    public RoomController(LibraryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return service.getAllRooms();
    }

    @GetMapping("/reservations")
    public List<RoomReservation> getAllRoomReservations() {
        return service.getAllRoomReservations();
    }

    @PostMapping("/{roomId}/reserve/{studentId}")
    public ResponseEntity<RoomReservation> reserveRoom(@PathVariable String roomId,
                                                       @PathVariable String studentId,
                                                       @RequestBody RoomReservation request) {
        RoomReservation reservation = service.reserveRoom(roomId, studentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @PutMapping("/reservations/{reservationId}/cancel")
    public ResponseEntity<RoomReservation> cancelReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(service.cancelRoomReservation(reservationId));
    }

    @PutMapping("/reservations/{reservationId}")
    public ResponseEntity<RoomReservation> modifyReservation(@PathVariable Integer reservationId,
                                                             @RequestBody RoomReservation request) {
        return ResponseEntity.ok(service.modifyRoomReservation(reservationId, request));
    }
}
