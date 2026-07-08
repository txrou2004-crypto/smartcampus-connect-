package com.smartcampus.reportinganalytics.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smartcampus.reportinganalytics.dto.CourseDTO;
import com.smartcampus.reportinganalytics.dto.NotificationDTO;
import com.smartcampus.reportinganalytics.dto.ReservationDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class AnalyticsService {

    @Autowired
    private RestTemplate restTemplate;

    private final String COURSE_SERVICE_URL = "http://localhost:8081/enrollments";
    private final String LIBRARY_SERVICE_URL = "http://localhost:8083/rooms/reservations";
    private final String NOTIFICATION_SERVICE_URL = "http://localhost:8082/notifications";

    // 1. ACADEMIC & ENROLLMENT ANALYTICS (Scarcity Table)
    @CircuitBreaker(name = "downstreamServices", fallbackMethod = "fallbackAcademicMetrics")
    public List<Map<String, Object>> getCourseSeatScarcityIndex() {
        // Appends /courses to hit the right endpoint on port 8081
        CourseDTO[] response = restTemplate.getForObject(COURSE_SERVICE_URL + "/courses", CourseDTO[].class);
        List<CourseDTO> courses = Arrays.asList(response != null ? response : new CourseDTO[0]);

        return courses.stream().map(course -> {
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("courseCode", course.getCourseCode());
            metrics.put("courseName", course.getCourseName());
            
            // Calculate scarcity percentage safely
            double fillRate = 0.0;
            if (course.getMaxCapacity() > 0) {
                fillRate = ((double) (course.getMaxCapacity() - course.getAvailableSeats()) / course.getMaxCapacity()) * 100;
            }
            metrics.put("fillRatePercentage", Math.round(fillRate));
            metrics.put("status", fillRate >= 85.0 ? "Filling Fast!" : "Available");
            return metrics;
        }).collect(Collectors.toList());
    }

    // 1b. GRADUATION PROGRESS RING
    @CircuitBreaker(name = "downstreamServices", fallbackMethod = "fallbackGraduationProgress")
    public Map<String, Object> getGraduationProgress(String studentId) {
        // Appends the accurate endpoint path mapping matching your group's enrollment controller
        String url = COURSE_SERVICE_URL + "/student/" + studentId + "/progress";
        Map<?, ?> response = restTemplate.getForObject(url, Map.class);
        
        Map<String, Object> metrics = new HashMap<>();
        if (response != null && response.containsKey("progressPercentage")) {
            metrics.put("studentId", studentId);
            metrics.put("progressPercentage", response.get("progressPercentage"));
        } else {
            // Internal fallback parameters if map returns incomplete data
            metrics.put("studentId", studentId);
            metrics.put("progressPercentage", 0);
        }
        return metrics;
    }

    // 2. LIBRARY OPERATIONS ANALYTICS (Flow Patterns)
    @CircuitBreaker(name = "downstreamServices", fallbackMethod = "fallbackLibraryMetrics")
    public Map<String, Object> getLibraryFlowTrends() {
        ReservationDTO[] response = restTemplate.getForObject(LIBRARY_SERVICE_URL, ReservationDTO[].class);
        List<ReservationDTO> reservations = Arrays.asList(response != null ? response : new ReservationDTO[0]);

        Map<String, Long> flowCounts = reservations.stream()
                .filter(r -> r.getReservationDate() != null)
                .collect(Collectors.groupingBy(r -> {
                    LocalDate date = LocalDate.parse(r.getReservationDate());
                    return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                }, Collectors.counting()));

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("flowTrends", flowCounts);
        metrics.put("recommendedQuietTime", flowCounts.getOrDefault("Wednesday", 0L) < flowCounts.getOrDefault("Monday", 0L) 
                   ? "Wednesday Afternoon" : "Friday Morning");
        return metrics;
    }

    // 3. COMMUNICATION PERFORMANCE ANALYTICS (Channel Distribution Ratio)
    @CircuitBreaker(name = "downstreamServices", fallbackMethod = "fallbackNotificationMetrics")
    public Map<String, Object> getCommunicationChannelRatio(String studentId) {
        // Perfectly maps to port 8082 /notifications/student/{student_id}
        NotificationDTO[] response = restTemplate.getForObject(NOTIFICATION_SERVICE_URL + "/student/" + studentId, NotificationDTO[].class);
        List<NotificationDTO> notifications = Arrays.asList(response != null ? response : new NotificationDTO[0]);

        Map<String, Long> channelDistribution = notifications.stream()
                .filter(n -> n.getType() != null)
                .collect(Collectors.groupingBy(NotificationDTO::getType, Collectors.counting()));

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("studentId", studentId);
        metrics.put("channelDistribution", channelDistribution);
        metrics.put("totalAlertsReceived", notifications.size());
        return metrics;
    }

    // --- RESILIENCE4J CIRCUIT BREAKER FALLBACKS ---
    public List<Map<String, Object>> fallbackAcademicMetrics(Throwable t) {
        System.out.println("Circuit Breaker Log -> Course Scarcity Service is down: " + t.getMessage());
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("status", "Circuit Breaker Active: Course Service Offline");
        return List.of(fallback);
    }

    public Map<String, Object> fallbackGraduationProgress(String studentId, Throwable t) {
        System.out.println("Circuit Breaker Log -> Graduation Progress path is down: " + t.getMessage());
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("studentId", studentId);
        fallback.put("progressPercentage", 0);
        fallback.put("status", "Circuit Breaker Active: Course Service Offline");
        return fallback;
    }

    public Map<String, Object> fallbackLibraryMetrics(Throwable t) {
        System.out.println("Circuit Breaker Log -> Library Service is down: " + t.getMessage());
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("status", "Circuit Breaker Active: Library Service Offline");
        fallback.put("flowTrends", new HashMap<>());
        return fallback;
    }

    public Map<String, Object> fallbackNotificationMetrics(String studentId, Throwable t) {
        System.out.println("Circuit Breaker Log -> Notification Service is down: " + t.getMessage());
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("studentId", studentId);
        fallback.put("status", "Circuit Breaker Active: Notification Service Offline");
        fallback.put("channelDistribution", new HashMap<>());
        return fallback;
    }
}
