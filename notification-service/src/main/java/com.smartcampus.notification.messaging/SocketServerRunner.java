package com.smartcampus.notification.messaging;

import com.smartcampus.notification.model.Notification;
import com.smartcampus.notification.repository.NotificationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;

@Component
public class SocketServerRunner implements CommandLineRunner {
    private final NotificationRepository repo;
    
    public SocketServerRunner(NotificationRepository repo) { 
        this.repo = repo; 
    }

    @Override
    public void run(String... args) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try (ServerSocket server = new ServerSocket(9999)) {
                System.out.println("Notification Socket Server started on port 9999");
                while (true) {
                    try (Socket client = server.accept();
                         BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                        
                        String msg = in.readLine();
                        if (msg != null && msg.startsWith("MSG:")) {
                            // Expected format: MSG:SOURCE:STUDENT_ID:ID_NUMBER:TYPE:CONTENT
                            String[] p = msg.split(":", 6);
                            if (p.length == 6) {
                                Notification n = new Notification();
                                String source = p[1].toUpperCase(); // E.g., ENROLMENT, BOOK_RESERVATION
                                n.setStudent_id(p[2]);
                                
                                // Route the ID to the correct column based on the SOURCE
                                try {
                                    Integer incomingId = Integer.parseInt(p[3]);
                                    
                                    if (source.equals("BOOK_RESERVATION")) {
                                        n.setReservation_id(incomingId);
                                    } else if (source.equals("ROOM_BOOKING")) {
                                        n.setRoom_reservation_id(incomingId);
                                    } else {
                                        // Default to enrolment if it's "ENROLMENT" or anything else
                                        n.setEnrolment_id(incomingId);
                                    }
                                } catch (NumberFormatException e) {
                                    // If parsing fails, leave the IDs as null
                                }
                                
                                n.setNotification_type(p[4]); 
                                n.setMessage(p[5]);
                                
                                repo.save(n);
                                System.out.println("Saved notification for student: " + p[2] + " from " + source);
                            }
                        }
                    }
                }
            } catch (IOException e) { 
                e.printStackTrace(); 
            }
        });
    }
}
