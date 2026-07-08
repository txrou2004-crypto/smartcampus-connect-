package com.smartcampus.notification.controller;

import com.smartcampus.notification.model.Notification;
import com.smartcampus.notification.repository.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*") // Fulfills the requirement: "Add CORS to enable front end"
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository repo;

    public NotificationController(NotificationRepository repo) {
        this.repo = repo;
    }

    // Operation A: Fetch Student Inbox
    @GetMapping("/student/{student_id}")
    public ResponseEntity<List<Notification>> getStudentNotifications(@PathVariable String student_id) {
        List<Notification> notifications = repo.findByStudent_id(student_id);
        if (notifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notifications);
    }

    // Operation B: Mark Notification as READ (Updates status)
    @PutMapping("/{notification_id}/read")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Integer notification_id) {
        Optional<Notification> opt = repo.findById(notification_id);
        if (opt.isPresent()) {
            Notification n = opt.get();
            // Assuming your frontend treats "SENT" as read, or you can use this to clear it
            n.setStatus("SENT"); 
            repo.save(n);
            return ResponseEntity.ok("Notification updated successfully");
        }
        return ResponseEntity.notFound().build();
    }
    
 
    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> allNotifications = repo.findAll();
        return ResponseEntity.ok(allNotifications);
    }
}
