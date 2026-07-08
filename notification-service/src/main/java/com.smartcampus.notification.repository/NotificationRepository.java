package com.smartcampus.notification.repository;

import com.smartcampus.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
    // The @Query annotation overrides Spring's guessing game
    @Query(value = "SELECT * FROM notifications WHERE student_id = ?1", nativeQuery = true)
    List<Notification> findByStudent_id(String student_id);
}
