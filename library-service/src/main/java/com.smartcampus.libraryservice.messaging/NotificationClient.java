package com.smartcampus.libraryservice.messaging;

import org.springframework.stereotype.Component;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class NotificationClient {
    
    // Single background thread to prevent network latency from blocking DB transactions
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // ADDED 'source' parameter here
    public void sendNotification(String source, String studentId, String referenceId, String type, String content) {
        executor.submit(() -> {
            // Strict Lab 5 framing: MSG:SOURCE:STUDENT_ID:REF_ID:TYPE:CONTENT
            // Updated to use the dynamic 'source' variable instead of hardcoded "LIBRARY"
            String framedMessage = String.format("MSG:%s:%s:%s:%s:%s", source, studentId, referenceId, type, content);
            
            try (Socket socket = new Socket("127.0.0.1", 9999);
                 PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {
                
                out.println(framedMessage);
                System.out.println("Notification sent to broker: " + framedMessage);
                
            } catch (Exception e) {
                // Swallows the error silently if the Notification service is down
                System.err.println("Notification broker offline. Skipping alert for " + studentId);
            }
        });
    }
}
