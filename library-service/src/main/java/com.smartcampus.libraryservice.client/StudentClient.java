package com.smartcampus.libraryservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class StudentClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String studentServiceUrl = "http://localhost:8080/students/";

    @CircuitBreaker(name = "studentService", fallbackMethod = "studentServiceFallback")
    public boolean isStudentActive(String studentId) {
        Map response = restTemplate.getForObject(studentServiceUrl + studentId, Map.class);

        if (response == null) {
            return false;
        }

        Object status = response.get("status");
        return status != null && status.toString().equalsIgnoreCase("ACTIVE");
    }

    public boolean studentServiceFallback(String studentId, Throwable throwable) {
        throw new RuntimeException("Student Service unavailable. Cannot verify student status.");
    }
}
