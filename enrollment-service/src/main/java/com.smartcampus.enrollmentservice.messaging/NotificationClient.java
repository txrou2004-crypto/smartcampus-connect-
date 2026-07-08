package com.smartcampus.enrollmentservice.messaging;

import org.springframework.stereotype.Component;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class NotificationClient {
    
    // Single background thread so network latency never blocks your DB transactions
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public void sendNotification(String studentId, String referenceId, String type, String content) {
        executor.submit(() -> {
            // Strict Lab 5 framing: MSG:SOURCE:STUDENT_ID:REF_ID:TYPE:CONTENT
            String framedMessage = String.format("MSG:ENROLLMENT:%s:%s:%s:%s", studentId, referenceId, type, content);
            
            try (Socket socket = new Socket("127.0.0.1", 9999);
                 PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {
                
                out.println(framedMessage);
                System.out.println("Notification sent to broker: " + framedMessage);
                
            } catch (Exception e) {
                // Swallows the error silently to protect the main service
                System.err.println("Notification broker offline. Skipping alert for " + studentId);
            }
        });
    }
}
