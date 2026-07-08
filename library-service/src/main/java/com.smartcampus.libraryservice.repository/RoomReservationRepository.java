package com.smartcampus.libraryservice.repository;

import com.smartcampus.libraryservice.model.RoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomReservationRepository extends JpaRepository<RoomReservation, Integer> {

    List<RoomReservation> findByStudentId(String studentId);

}
