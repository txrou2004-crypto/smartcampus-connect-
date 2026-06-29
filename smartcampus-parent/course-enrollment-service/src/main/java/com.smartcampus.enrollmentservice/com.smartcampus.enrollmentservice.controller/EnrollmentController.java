package com.smartcampus.enrollmentservice.controller;

import com.smartcampus.enrollmentservice.model.Course;
import com.smartcampus.enrollmentservice.model.Enrollment;
import com.smartcampus.enrollmentservice.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    // Admin register courses
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerCourse(course));
    }
    
    // Student view course catalog
    @GetMapping("/courses")
    public List<Course> viewCatalog() {
        return service.getCourseCatalog();
    }

    // Student add / perform capacity check and enroll course (or waitlist if full)
    @PostMapping
    public CompletableFuture<ResponseEntity<Enrollment>> addEnrollment(@RequestBody Map<String, String> payload) {
        String studentId = payload.get("student_id");
        String courseId = payload.get("course_id");
        
        return service.submitConcurrentEnrollment(studentId, courseId)
                .thenApply(res -> {
                    if ("WAITLIST".equals(res.getStatus())) {
                        return ResponseEntity.status(HttpStatus.OK).body(res);
                    }
                    return ResponseEntity.status(HttpStatus.CREATED).body(res);
                });
    }

    // Enroll into waitlist if the course available seats is full
    @PostMapping("/waitlist")
    public CompletableFuture<ResponseEntity<Enrollment>> enrollWaitlist(@RequestBody Map<String, String> payload) {
        String studentId = payload.get("student_id");
        String courseId = payload.get("course_id");

        if (studentId == null || courseId == null) {
            throw new IllegalArgumentException("Missing student_id or course_id in request body.");
        }

        return service.enrollWaitlist(studentId, courseId)
                .thenApply(res -> ResponseEntity.status(HttpStatus.CREATED).body(res));
    }

    // Drop enrollment if students want
    @PutMapping("/drop")
    public ResponseEntity<Enrollment> dropEnrollment(@RequestBody Map<String, String> payload) {
        String studentId = payload.get("student_id");
        String courseId = payload.get("course_id");
        return ResponseEntity.ok(service.dropEnrollment(studentId, courseId));
    }

    // Let students view student schedule after enrollment 
    @GetMapping("/student/{studentId}")
    public List<Enrollment> viewSchedule(@PathVariable String studentId) {
        return service.getStudentSchedule(studentId);
    }
    
    // Defer a student's enrollment
    @PutMapping("/defer")
    public ResponseEntity<Enrollment> deferEnrollment(@RequestBody Map<String, String> payload) {
        String studentId = payload.get("student_id");
        String courseId = payload.get("course_id");
        
        if (studentId == null || courseId == null) {
            throw new IllegalArgumentException("Missing student_id or course_id in request body.");
        }
        
        return ResponseEntity.ok(service.changeEnrollmentStatus(studentId, courseId, "DEFERRED"));
    }

    // Withdraw a student from a course
    @PutMapping("/withdraw")
    public ResponseEntity<Enrollment> withdrawEnrollment(@RequestBody Map<String, String> payload) {
        String studentId = payload.get("student_id");
        String courseId = payload.get("course_id");
        
        if (studentId == null || courseId == null) {
            throw new IllegalArgumentException("Missing student_id or course_id in request body.");
        }
        
        return ResponseEntity.ok(service.changeEnrollmentStatus(studentId, courseId, "WITHDRAWN"));
    }
    
 // View waitlist array for an isolated course ID structure
    @GetMapping("/course/{courseId}/waitlist")
    public ResponseEntity<List<Enrollment>> viewCourseWaitlist(@PathVariable String courseId) {
        return ResponseEntity.ok(service.getCourseWaitlist(courseId));
    }
}
