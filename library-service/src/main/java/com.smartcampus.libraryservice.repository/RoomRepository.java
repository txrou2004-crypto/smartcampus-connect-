package com.smartcampus.libraryservice.repository;

import com.smartcampus.libraryservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
}
